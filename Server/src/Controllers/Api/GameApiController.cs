using AutoMapper;
using src.Controllers.Abstract;
using src.Helpers.Api.Response;
using src.Models.Api;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web.Http;
using src.Helpers.Api.Results;
using src.Models;
using System.Data.Entity;
using src.Helpers.PushNotifications;

namespace src.Controllers.Api
{
    [RoutePrefix("api/1/games")]
    public class GameApiController : AbstractApiController
    {
        [HttpGet, Route("")]
        public async Task<IHttpActionResult> GetAll()
        {
            var games = this.db.Games.Include(x => x.ChallengerOne).Include(x => x.ChallengerTwo);
            return this.Ok(new ApiResponse(200, Mapper.Map<IEnumerable<GameApiModel>>(games).ToArray()));
        }

        [HttpGet, Route("{id:long}")]
        public async Task<IHttpActionResult> GetSingle(long id)
        {
            var wanted = await this.db.Games.Where(x => x.Id.Equals(id)).Include(x => x.ChallengerOne).Include(x => x.ChallengerTwo).FirstAsync();
            if (wanted != null)
                return this.Ok(new ApiResponse(200, Mapper.Map<GameApiModel>(wanted)));
            return this.NotFound(new ApiResponse(404, id));
        }

        [HttpPost, Route(""), Authorize(Roles = "admin,superadmin")]
        public async Task<IHttpActionResult> CreateGame(CreateGameApiModel model)
        {
            if (ModelState.IsValid && model != null)
            {
                var teamOne = await this.db.Teams.FindAsync(model.ChallengerOne);
                var teamtwo = await this.db.Teams.FindAsync(model.ChallengerTwo);

                if (!model.Locations.Length.Equals(0))
                {
                    teamOne.Latitude = model.Locations[0].Lat;
                    teamOne.Latitude = model.Locations[0].Lng;

                    teamOne.Latitude = model.Locations[1].Lat;
                    teamOne.Latitude = model.Locations[1].Lng;
                }

                var mapped = new Game()
                {
                    ChallengerOne = teamOne,
                    ChallengerTwo = teamtwo,
                    Length = model.Length,
                    MapId = model.MapId,
                    Started = false,
                    Finished = false
                };

                this.db.Games.Add(mapped);

                await this.db.SaveChangesAsync();

                return this.Ok(new ApiResponse(200, Mapper.Map<GameApiModel>(mapped)));
            }
            return this.BadRequest(new ApiResponse(400, model));
        }

        [HttpPost, Route("start/{id:long}"), Authorize(Roles = "admin,superadmin")]
        public async Task<IHttpActionResult> StartGame(long id)
        {
            var wanted = await this.db.Games.FindAsync(id);
            if (wanted != null)
            {
                wanted.Started = true;
                wanted.Start = DateTime.Now;
                await this.db.SaveChangesAsync();

                List<User> contestants = wanted.ChallengerOne.Users.ToList();
                contestants.AddRange(wanted.ChallengerTwo.Users.ToList());

                foreach (var user in contestants)
                {
                    new GcmProvider().CreateNotification(new PushNotificationData
                    {
                        Action = 2,
                        Message = "Game started!",
                        Data = new
                        {
                            GameId = wanted.Id
                        }
                    }, user.RegistrationId).SendAsync().Wait();
                }

                return this.Ok(new ApiResponse(200, Mapper.Map<GameApiModel>(wanted)));
            }
            return this.NotFound(new ApiResponse(404, id));
        }

        [HttpPost, Route("ban/{id}"), Authorize(Roles = "admin,superadmin")]
        public async Task<IHttpActionResult> BanPlayer(string id)
        {
            var wanted = await this.userManager.FindByIdAsync(id);
            if (wanted != null)
            {
                wanted.Banned = true;

                var result = await this.userManager.UpdateAsync(wanted);

                if (result.Succeeded)
                {
                    new GcmProvider().CreateNotification(new PushNotificationData
                    {
                        Action = 4,
                        Message = "You have been banned!",
                        Data = new
                        {
                            UserId = wanted.Id
                        }
                    }, wanted.RegistrationId).SendAsync().Wait();

                    return this.Ok(new ApiResponse(200, Mapper.Map<UserApiModel>(wanted)));
                }
                return this.InternalServerError(new ApiResponse(500, wanted));
            }
            return this.NotFound(new ApiResponse(404, id));
        }

        [HttpPost, Route("end/{id:long}"), Authorize(Roles = "admin,superadmin")]
        public async Task<IHttpActionResult> EndGame(long id)
        {
            var wanted = await this.db.Games.FindAsync(id);
            if (wanted != null)
            {
                wanted.Finished = true;
                wanted.End = DateTime.Now;
                await this.db.SaveChangesAsync();

                List<User> contestants = wanted.ChallengerOne.Users.ToList();
                contestants.AddRange(wanted.ChallengerTwo.Users.ToList());

                foreach (var user in contestants)
                {
                    new GcmProvider().CreateNotification(new PushNotificationData
                    {
                        Action = 2,
                        Message = "Game ended!",
                        Data = new
                        {
                            GameId = wanted.Id
                        }
                    }, user.RegistrationId).SendAsync().Wait();
                }

                return this.Ok(new ApiResponse(200, Mapper.Map<GameApiModel>(wanted)));
            }
            return this.NotFound(new ApiResponse(404, id));
        }

        [HttpPut, Route(""), Authorize]
        public async Task<IHttpActionResult> UpdateLocation(LocationApiModel model)
        {
            if (ModelState.IsValid && model != null)
            {
                this.CurrentUser.Longitude = model.Long;
                this.CurrentUser.Latitude = model.Lat;

                var result = await this.userManager.UpdateAsync(this.CurrentUser);

                var map = await this.db.Maps.FindAsync(model.MapId);

                if (!this.IsPointInPolygon(map.Coordinates.ToList(), model.Long, model.Lat))
                {
                    new GcmProvider().CreateNotification(new PushNotificationData
                    {
                        Action = 5,
                        Message = "Out of boundaries!",
                        Data = new
                        {
                            MapId = map.Id
                        }
                    }, this.CurrentUser.RegistrationId).SendAsync().Wait();
                }

                if (result.Succeeded)
                    return this.Ok(new ApiResponse(200, Mapper.Map<UserApiModel>(this.CurrentUser)));
                return this.InternalServerError(new ApiResponse(500, Mapper.Map<UserApiModel>(this.CurrentUser)));
            }
            return this.BadRequest(new ApiResponse(400, model));
        }

        [HttpPut, Route("nfc"), Authorize]
        public async Task<IHttpActionResult> SetNFC(PlayerMarkedApiModel model)
        {
            if (ModelState.IsValid && model != null)
            {
                this.CurrentUser.NFC = model.NFC;
                this.CurrentUser.Killed = false;
                this.CurrentUser.Banned = false;

                var result = await this.userManager.UpdateAsync(this.CurrentUser);

                if (result.Succeeded)
                    return this.Ok(new ApiResponse(200, Mapper.Map<UserApiModel>(this.CurrentUser)));
                return this.InternalServerError(new ApiResponse(500, model));
            }
            return this.BadRequest(new ApiResponse(400, model));
        }

        [HttpPut, Route("marked"), Authorize]
        public async Task<IHttpActionResult> PlayerMarked(PlayerMarkedApiModel model)
        {
            if (ModelState.IsValid && model != null)
            {
                if (this.CurrentUser.NFC.Equals(model.NFC))
                {
                    this.CurrentUser.Killed = true;

                    var result = await this.userManager.UpdateAsync(this.CurrentUser);

                    if (result.Succeeded)
                    {
                        var game = await this.db.Games.FindAsync(model.GameId);

                        int myTeam = 0;

                        int enemyTeam = 0;

                        if (game.ChallengerOne.Users.Any(x => x.Id.Equals(this.CurrentUser.Id)))
                        {
                            foreach (var user in game.ChallengerOne.Users.Where(x => !x.Killed && !x.Banned))
                            {
                                myTeam += 1;
                            }
                            foreach (var user in game.ChallengerTwo.Users.Where(x => !x.Killed && !x.Banned))
                            {
                                enemyTeam += 1;
                            }
                        }
                        else
                        {
                            foreach (var user in game.ChallengerTwo.Users.Where(x => !x.Killed && !x.Banned))
                            {
                                enemyTeam += 1;
                            }
                            foreach (var user in game.ChallengerOne.Users.Where(x => !x.Killed && !x.Banned))
                            {
                                myTeam += 1;
                            }
                        }

                        new GcmProvider().CreateNotification(new PushNotificationData
                        {
                            Action = 3,
                            Message = "Player killed!",
                            Data = new
                            {
                                MyTeam = myTeam,
                                EnemyTeam = enemyTeam
                            }
                        }, this.CurrentUser.RegistrationId).SendAsync().Wait();
                        return this.Ok(new ApiResponse(200, Mapper.Map<UserApiModel>(this.CurrentUser)));
                    }
                    return this.InternalServerError(new ApiResponse(500, model));
                }
            }
            return this.BadRequest(new ApiResponse(400, model));
        }

        [HttpGet, Route("summary/{id:long}")]
        public async Task<IHttpActionResult> MatchSummary(long id)
        {
            var wanted = this.db.Games.Where(x => x.Id.Equals(id)).Include(x => x.ChallengerOne).Include(x => x.ChallengerTwo);
            if (!(await wanted.CountAsync()).Equals(0))
            {
                var game = await wanted.FirstAsync();
                int totalKills = 0;
                foreach (User user in game.ChallengerOne.Users)
                {
                    if (user.Killed)
                    {
                        totalKills += 1;
                    }
                }
                foreach (User user in game.ChallengerTwo.Users)
                {
                    if (user.Killed)
                    {
                        totalKills += 1;
                    }
                }

                var summary = new MatchSummaryApiModel()
                {
                    Length = game.Length,
                    ChallengerOne = Mapper.Map<TeamApiModel>(game.ChallengerOne),
                    ChallengerTwo = Mapper.Map<TeamApiModel>(game.ChallengerTwo),
                    TotalKills = totalKills
                };

                return this.Ok(new ApiResponse(200, summary));
            }
            return this.NotFound(new ApiResponse(404, id));
        }

        private bool IsPointInPolygon(List<Coords> poly, double longitude, double latitude)
        {
            int i, j;
            bool c = false;
            for (i = 0, j = poly.Count - 1; i < poly.Count; j = i++)
            {
                if ((((poly[i].Lat <= latitude) && (latitude < poly[j].Lat)) |
                    ((poly[j].Lat <= latitude) && (latitude < poly[i].Lat))) &&
                    (longitude < (poly[j].Lng - poly[i].Lng) * (latitude - poly[i].Lat) / (poly[j].Lat - poly[i].Lat) + poly[i].Lng))
                    c = !c;
            }
            return c;
        }
    }
}
