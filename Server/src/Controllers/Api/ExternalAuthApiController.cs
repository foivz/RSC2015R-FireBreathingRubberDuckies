using src.Controllers.Abstract;
using src.Models.Api;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web.Configuration;
using System.Web.Http;
using src.Helpers.Api.Authentication.External.Extensions;
using src.Helpers.Api.Authentication.External.Models;
using src.Helpers.Api.Authentication.External.Interfaces;
using src.Helpers.Api.Authentication.External;
using Microsoft.AspNet.Identity;
using System.Security.Claims;
using Microsoft.Owin.Security.OAuth;
using Microsoft.Owin.Security;
using src.Helpers.Api.Response;
using src.Helpers.Api.Results;
using System.Data.Entity;
using src.Helpers.Email;
using src.Helpers.Api.Authentication.Tokens.Interfaces;
using src.Helpers.Api.Authentication.Tokens;

namespace src.Controllers.Api
{
    //[RoutePrefix("api/1/external")]
    public class ExternalAuthApiController : AbstractApiController
    {
        private IExternalProvider<ExternalResponseModel, FacebookProfileModel> fbProvider { get; set; }
        private IExternalProvider<ExternalResponseModel, GoogleProfileModel> googleProvider { get; set; }
        public ExternalAuthApiController()
            : this(new FacebookExternalProvider(), new GoogleExternalProvider())
        { }

        public ExternalAuthApiController(IExternalProvider<ExternalResponseModel, FacebookProfileModel> fbProvider, IExternalProvider<ExternalResponseModel, GoogleProfileModel> googleProvider)
        {
            this.fbProvider = fbProvider;
            this.googleProvider = googleProvider;
        }

        [HttpGet, Route("api/1/external/facebook")]
        public IHttpActionResult FacebookProvider()
        {
            var facebookProvider = string.Format("https://www.facebook.com/v2.5/dialog/oauth?response_type=code&client_id={0}&redirect_uri={1}&display_type=popup&scope=email", WebConfigurationManager.AppSettings["FacebookClientId"], WebConfigurationManager.AppSettings["RedirectUri"]);

            return Redirect(facebookProvider);
        }

        [HttpPost, Route("auth/facebook")]
        public async Task<IHttpActionResult> RegisterExternalFacebook(ExternalLoginModel model)
        {
            FacebookProfileModel profile = null;

            if (model.AccessToken != null)
                profile = await fbProvider.GetProfileData(model.AccessToken);
            else if (model.Code != null)
                profile = await model.GetProfile(this.fbProvider);
            else
                return this.BadRequest(new ApiResponse(400));

            if (!await this.db.Users.AnyAsync(x => x.Email.Equals(profile.Email)))
            {
                var toCreate = new Models.User
                {
                    FirstName = profile.First_Name,
                    LastName = profile.Last_Name,
                    Email = profile.Email,
                    UserName = profile.Name,
                    EmailConfirmed = true,
                    Avatar = profile.Picture.Data.Url,
                    RegistrationId = model.RegistrationId,
                    Enabled = true
                };

                var result = await userManager.CreateAsync(toCreate);

                if (result.Succeeded)
                    result = await userManager.AddToRoleAsync(toCreate.Id, "user");

                if (!result.Succeeded)
                    return this.InternalServerError(new ApiResponse(500));
            }

            var localUser = this.db.Users.FirstOrDefault(x => x.Email.Equals(profile.Email));

            if (localUser != null)
            {
                localUser.RegistrationId = model.RegistrationId;

                var result = await this.userManager.UpdateAsync(localUser);

                if (!localUser.Logins.Any(x => x.LoginProvider.Equals("facebook") && x.ProviderKey.Equals(profile.Id)))
                {
                    var addLogin = await userManager.AddLoginAsync(localUser.Id, new UserLoginInfo("facebook", profile.Id));

                    if (!addLogin.Succeeded)
                    {
                        return this.InternalServerError(new ApiResponse(500));
                    }
                }

                var role = (await this.userManager.GetRolesAsync(localUser.Id)).First();

                ITokenBuilder builder = new TokenBuilder(localUser.Id, localUser.UserName, role);

                return Ok(new
                {
                    Id = localUser.Id,
                    Role = role,
                    Token = builder.GetLocalAccessToken()
                });
            }
            return this.NotFound(new ApiResponse(404));
        }

        [HttpPost, Route("auth/google")]
        public async Task<IHttpActionResult> RegisterExternalGoogle(ExternalLoginModel model)
        {
            GoogleProfileModel profile = null;

            if (model.AccessToken != null)
                profile = await googleProvider.GetProfileData(model.AccessToken);
            else if (model.Code != null)
                profile = await model.GetProfile(this.googleProvider);
            else
                return this.BadRequest(new ApiResponse(400));

            if (!await this.db.Users.AnyAsync(x => x.Email.Equals(profile.Email)))
            {
                var toCreate = new Models.User
                {
                    FirstName = profile.Given_Name,
                    LastName = profile.Family_Name,
                    Email = profile.Email,
                    UserName = profile.Name,
                    EmailConfirmed = true,
                    RegistrationId = model.RegistrationId,
                    Avatar = profile.Picture,
                    Enabled = true
                };

                var result = await userManager.CreateAsync(toCreate);

                if (result.Succeeded)
                    result = await userManager.AddToRoleAsync(toCreate.Id, "user");

                if (!result.Succeeded)
                    return this.InternalServerError(new ApiResponse(500));
            }

            var localUser = this.db.Users.FirstOrDefault(x => x.Email.Equals(profile.Email));

            if (localUser != null)
            {
                localUser.RegistrationId = model.RegistrationId;

                var result = await this.userManager.UpdateAsync(localUser);

                if (!localUser.Logins.Any(x => x.LoginProvider.Equals("facebook") && x.ProviderKey.Equals(profile.Sub)))
                {
                    var addLogin = await userManager.AddLoginAsync(localUser.Id, new UserLoginInfo("facebook", profile.Sub));

                    if (!addLogin.Succeeded)
                    {
                        return this.InternalServerError(new ApiResponse(500));
                    }
                }

                var role = (await this.userManager.GetRolesAsync(localUser.Id)).First();

                ITokenBuilder builder = new TokenBuilder(localUser.Id, localUser.UserName, role);

                return Ok(new
                {
                    Id = localUser.Id,
                    Role = role,
                    Token = builder.GetLocalAccessToken()
                });
            }
            return this.NotFound(new ApiResponse(404));
        }
    }
}
