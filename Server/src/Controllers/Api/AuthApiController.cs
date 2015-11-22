using src.Controllers.Abstract;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web.Http;
using src.Helpers.Api.Results;
using src.Models.Api;
using System.Web;
using AutoMapper;
using src.Models;
using src.Helpers.Api.Response;

namespace src.Controllers.Api
{
    [RoutePrefix("api/1")]
    public class AuthApiController : AbstractApiController
    {
        [HttpPost, Route("register")]
        public async Task<IHttpActionResult> Register(RegisterApiModel model)
        {
            if (ModelState.IsValid && model != null)
            {
                var user = Mapper.Map<RegisterApiModel, User>(model);
                user.Id = Guid.NewGuid().ToString();
                user.Enabled = true;
                var result = await this.userManager.CreateAsync(user, model.Password);
                if (result.Succeeded)
                {
                    if ((await this.userManager.AddToRoleAsync(user.Id, "user")).Succeeded)
                    {
                        var confirmToken = await this.userManager.GenerateEmailConfirmationTokenAsync(user.Id);

                        await userManager.SendEmailAsync(user.Id, "Confirmation email!", string.Format("Activate your account by clicking here: {0}/confirm?id={1}&token={2}",
                             HttpContext.Current.Request.Url.Scheme + "://" + HttpContext.Current.Request.Url.Authority, user.Id, HttpUtility.UrlEncode(confirmToken)));

                        return Ok(new ApiResponse(200, Mapper.Map<UserApiModel>(user)));
                    }
                }

                else
                    return this.BadRequest(new ApiResponse(400, Mapper.Map<UserApiModel>(model)));
            }
            return this.BadRequest(new ApiResponse(400, Mapper.Map<UserApiModel>(model)));
        }
    }
}
