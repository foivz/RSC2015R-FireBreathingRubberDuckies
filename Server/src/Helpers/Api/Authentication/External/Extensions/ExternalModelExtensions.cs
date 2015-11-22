using src.Helpers.Api.Authentication.External.Interfaces;
using src.Helpers.Api.Authentication.External.Models;
using src.Models.Api;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Web;

namespace src.Helpers.Api.Authentication.External.Extensions
{
    public static class ExternalModelExtensions
    {
        public static async Task<FacebookProfileModel> GetProfile(this ExternalLoginModel model, IExternalProvider<ExternalResponseModel, FacebookProfileModel> provider)
        {
            return await provider.GetProfileData((await provider.ExchangeCodeForAccessToken(model.Code)).Access_Token);
        }

        public static async Task<GoogleProfileModel> GetProfile(this ExternalLoginModel model, IExternalProvider<ExternalResponseModel, GoogleProfileModel> provider)
        {
            return await provider.GetProfileData((await provider.ExchangeCodeForAccessToken(model.Code)).Access_Token);
        }
    }
}