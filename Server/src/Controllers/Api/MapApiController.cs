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
    }
}
