using src.Controllers.Abstract;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web.Http;
using src.Helpers.Api.Results;
using src.Models;
using src.Helpers.Api.Response;
using System.Data.Entity;

namespace src.Controllers.Api
{
    [RoutePrefix("api/1/maps")]
    public class MapApiController : AbstractApiController
    {
        [HttpPost, Route(""), Authorize(Roles = "admin,superadmin")]
        public async Task<IHttpActionResult> SendCoords(Coords[] coords)
        {
            if (ModelState.IsValid)
            {
                var model = coords.ToList();

                var map = new Map();

                foreach (var coord in model)
                {
                    map.Coordinates.Add(coord);
                }

                this.db.Maps.Add(map);

                await this.db.SaveChangesAsync();

                return this.Ok(new ApiResponse(200, map));
            }
            return this.BadRequest(new ApiResponse(400));
        }

        [HttpGet, Route("{id:long}"), Authorize(Roles = "admin,superadmin")]
        public async Task<IHttpActionResult> GetMap(long id)
        {
            var wanted = await this.db.Maps.FindAsync(id);
            if (wanted != null)
            {
                return this.Ok(new ApiResponse(200, wanted));
            }
            return this.NotFound(new ApiResponse(404, id));
        }

        /*[HttpGet, Route("teammates/{id:long}")]
        public async Task<IHttpActionResult> GetTeamMatesCoords(long id)
        {
            var team = await this.db.Teams.Where(x => x.Id.Equals(id)).Include(x => x.Users).FirstAsync();

            List<object> coordinates = new List<object>();

            foreach (var user in team.Users)
            {
                coordinates.Add(new
                {
                    lat = user.Latitude,
                    lng = user.Longitude
                });
            }

            return this.Ok(coordinates);
        }*/

        [HttpGet, Route("teammates/{id:long}/{gameId:long}")]
        public async Task<IHttpActionResult> GetTeamMatesCoords(long id, long gameId)
        {
            if (id != 0)
            {
                var team = await this.db.Teams.Where(x => x.Id.Equals(id)).Include(x => x.Users).FirstAsync();

                List<object> coordinates = new List<object>();

                foreach (var user in team.Users)
                {
                    coordinates.Add(new
                    {
                        lat = user.Latitude,
                        lng = user.Longitude
                    });
                }

                return this.Ok(coordinates);
            }
            else
            {
                var game = await this.db.Games.Where(x => x.Id.Equals(gameId)).Include(x => x.ChallengerOne).Include(x => x.ChallengerTwo).FirstAsync();

                List<object> coordinatesOne = new List<object>();
                List<object> coordinatesTwo = new List<object>();

                if (game.ChallengerOne != null)
                    foreach (var user in game.ChallengerOne.Users)
                    {
                        coordinatesOne.Add(new
                        {
                            lat = user.Latitude,
                            lng = user.Longitude
                        });
                    };

                if (game.ChallengerTwo != null)
                    foreach (var user in game.ChallengerTwo.Users)
                    {
                        coordinatesTwo.Add(new
                        {
                            lat = user.Latitude,
                            lng = user.Longitude
                        });
                    };
                return this.Ok(new
                {
                    TeamOne = coordinatesOne.ToArray(),
                    TeamTwo = coordinatesTwo.ToArray()
                });

            }
        }
    }
}
