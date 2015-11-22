using Newtonsoft.Json;
using src.Helpers.Api.Authentication.External.Abstract;
using src.Helpers.Api.Authentication.External.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Configuration;
using Flurl;
using Flurl.Http;

namespace src.Helpers.Api.Authentication.External
{
    public class FacebookExternalProvider : AbstractExternalProvider<ExternalResponseModel, FacebookProfileModel>
    {
        private const string TokenUri = "https://graph.facebook.com/v2.5/oauth/access_token";
        private const string ProfileUri = "https://graph.facebook.com/v2.5/me?fields=id,email,first_name,last_name,link,name,picture.width(400).height(400)";

        public FacebookExternalProvider()
        { }

        public override async Task<ExternalResponseModel> ExchangeCodeForAccessToken(string code)
        {
            var pathSegments = new
            {
                client_id = WebConfigurationManager.AppSettings["FacebookClientId"],
                client_secret = WebConfigurationManager.AppSettings["FacebookSecret"],
                redirect_uri = WebConfigurationManager.AppSettings["RedirectUri"],
                code = code,
            };

            var response = await TokenUri.SetQueryParams(pathSegments).GetAsync();

            var content = await response.Content.ReadAsStringAsync();

            var jsonObject = await Task.Factory.StartNew(() => JsonConvert.DeserializeObject<ExternalResponseModel>(content));

            return jsonObject;
        }

        public override async Task<FacebookProfileModel> GetProfileData(string accessToken)
        {
            var pathSegments = new
            {
                access_token = accessToken
            };

            var response = await ProfileUri.SetQueryParams(pathSegments).GetAsync();

            var content = await response.Content.ReadAsStringAsync();

            var jsonObject = await Task.Factory.StartNew(() => JsonConvert.DeserializeObject<FacebookProfileModel>(content));

            return jsonObject;
        }
    }
}