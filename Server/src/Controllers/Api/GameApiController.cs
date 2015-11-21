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

namespace src.Controllers.Api
{
    [RoutePrefix("api/1/games")]
    public class GameApiController : AbstractApiController
    {
        [HttpGet, Route("")]
        public async Task<IHttpActionResult> GetAll()
        {
            var games = this.db.Games;
            return this.Ok(new ApiResponse(200, Mapper.Map<IEnumerable<GameApiModel>>(games).ToArray()));
        }

        [HttpGet, Route("{id:long}")]
        public async Task<IHttpActionResult> GetSingle(long id)
        {
            var wanted = await this.db.Games.FindAsync(id);
            if (wanted != null)
                return this.Ok(new ApiResponse(200, Mapper.Map<GameApiModel>(wanted)));
            return this.NotFound(new ApiResponse(404, id));
        }

        [HttpPost, Route(""), Authorize(Roles="admin,superadmin")]
        public async Task<IHttpActionResult> CreateGame(CreateGameApiModel model)
        {
            if (ModelState.IsValid && model != null)
            {
                var mapped = new Game(){
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
    }
}
