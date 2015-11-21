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
                var mapped = new Game()
                {
                    ChallengerOne = await this.db.Teams.FindAsync(model.ChallengerOne),
                    ChallengerTwo = await this.db.Teams.FindAsync(model.ChallengerTwo),
                    Length = model.Length,
                    Started = false,
                    Finished = false
                };

                this.db.Games.Add(mapped);

                await this.db.SaveChangesAsync();

                return this.Ok(new ApiResponse(200, Mapper.Map<GameApiModel>(mapped)));
            }
            return this.BadRequest(new ApiResponse(400, model));
        }

        [HttpPut, Route(""), Authorize]
        public async Task<IHttpActionResult> UpdateLocation(LocationApiModel model)
        {
            if (ModelState.IsValid && model != null)
            {
                this.CurrentUser.Longitude = model.Long;
                this.CurrentUser.Latitude = model.Lat;

                var result = await this.userManager.UpdateAsync(this.CurrentUser);

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
                        return this.Ok(new ApiResponse(200, Mapper.Map<UserApiModel>(this.CurrentUser)));
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
    }
}
