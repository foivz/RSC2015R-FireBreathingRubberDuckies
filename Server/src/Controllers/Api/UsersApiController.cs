using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web.Http;
using src.Helpers.Api.Results;
using AutoMapper;
using src.Models.Api;
using src.Controllers.Abstract;
using src.Helpers.Api.Response;
using src.Models;

namespace src.Controllers.Api
{
    [RoutePrefix("api/1/users")]
    public class UsersApiController : AbstractApiController
    {
        [HttpGet, Route(""), Authorize(Roles = "admin,superadmin")]
        public IHttpActionResult GetAll()
        {
            return Ok(new ApiResponse(200, Mapper.Map<IEnumerable<UserApiModel>>(this.db.Users.AsEnumerable()).ToArray()));
        }

        [HttpGet, Route("{id}"), Authorize]
        public IHttpActionResult Get(string id)
        {
            var wanted = this.db.Users.Find(id);
            if (wanted != null)
                return Ok(new ApiResponse(200, Mapper.Map<UserApiModel>(wanted)));
            return this.NotFound(new ApiResponse(404, id));
        }

        [HttpGet, Route("profile"), Authorize]
        public IHttpActionResult Profile()
        {
            var mapped = Mapper.Map<UserApiModel>(this.CurrentUser);
            return this.Ok(new ApiResponse(200, mapped));
        }

        [HttpPost, Route(""), Authorize(Roles = "admin,superadmin")]
        public async Task<IHttpActionResult> Post(CreateUserApiModel model)
        {
            if (ModelState.IsValid && model != null)
            {
                var user = Mapper.Map<User>(model);
                user.Enabled = true;
                user.EmailConfirmed = true;

                var result = await this.userManager.CreateAsync(user, model.Password);

                if (result.Succeeded)
                    if ((await this.userManager.AddToRoleAsync(user.Id, "user")).Succeeded)
                        return this.Ok(new ApiResponse(200, Mapper.Map<UserApiModel>(user)));

                return this.InternalServerError(new ApiResponse(500, Mapper.Map<UserApiModel>(user)));
            }
            return this.BadRequest(new ApiResponse(400, model));
        }

        [HttpPost, Route("judge"), Authorize(Roles = "superadmin")]
        public async Task<IHttpActionResult> CreateJudge(CreateUserApiModel model)
        {
            if (ModelState.IsValid && model != null)
            {
                var user = Mapper.Map<User>(model);
                user.Enabled = true;
                user.EmailConfirmed = true;

                var result = await this.userManager.CreateAsync(user, model.Password);

                if (result.Succeeded)
                    if ((await this.userManager.AddToRoleAsync(user.Id, "admin")).Succeeded)
                        return this.Ok(new ApiResponse(200, Mapper.Map<UserApiModel>(user)));

                return this.InternalServerError(new ApiResponse(500, Mapper.Map<UserApiModel>(user)));
            }
            return this.BadRequest(new ApiResponse(400, model));
        }

        [HttpPut, Route("{id}"), Authorize]
        public async Task<IHttpActionResult> Update(string id, UserApiModel model)
        {
            if (ModelState.IsValid && model != null)
            {
                if (id.Equals(model.Id))
                {
                    var wantedUser = this.db.Users.Find(model.Id);

                    if (wantedUser != null)
                    {
                        Mapper.Map<UserApiModel, User>(model, wantedUser, opts => opts.BeforeMap((s, d) => s.Enabled = d.Enabled));

                        var result = await this.userManager.UpdateAsync(wantedUser);

                        if (result.Succeeded)
                            return this.Ok(new ApiResponse(200, model));
                        else
                            return this.Conflict(new ApiResponse(409, model));
                    }
                    return this.NotFound(new ApiResponse(404, id));
                }
            }
            return this.BadRequest(new ApiResponse(400, model));
        }

        [HttpPut, Route("disable/{id}"), Authorize(Roles = "admin,superadmin")]
        public async Task<IHttpActionResult> DisableAccount(string id)
        {
            var wanted = this.db.Users.Find(id);

            if (wanted != null)
            {
                wanted.Enabled = !wanted.Enabled;
                var result = await this.userManager.UpdateAsync(wanted);
                if (result.Succeeded)
                {
                    return this.Ok(new ApiResponse(200, Mapper.Map<UserApiModel>(wanted)));
                }
                return this.InternalServerError(new ApiResponse(500, id));
            }
            return this.NotFound(new ApiResponse(404, id));
        }
    }
}
