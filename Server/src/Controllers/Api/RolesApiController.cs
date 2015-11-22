using src.Controllers.Abstract;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web.Http;
using System.Data.Entity;
using AutoMapper;
using src.Models.Api;
using src.Helpers.Api.Response;
using src.Helpers.Api.Results;

namespace src.Controllers.Api
{
    [RoutePrefix("api/1/roles")]
    public class RolesApiController : AbstractApiController
    {
        [HttpGet, Route(""), Authorize(Roles = "admin,superadmin")]
        public IHttpActionResult GetAll()
        {
            return Ok(new ApiResponse(200, Mapper.Map<IEnumerable<RoleApiModel>>(this.db.Roles).ToArray()));
        }

        [HttpGet, Route("{id}"), Authorize(Roles = "admin,superadmin")]
        public IHttpActionResult Get(string id)
        {
            var wanted = this.db.Roles.Find(id);
            if (wanted != null)
                return Ok(new ApiResponse(200, Mapper.Map<RoleApiModel>(wanted)));
            return this.NotFound(new ApiResponse(404, id));
        }
    }
}
