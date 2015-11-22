using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using src.Helpers.Api.Authentication.External.Abstract;
using src.Helpers.Api.Authentication.External.Models;
using System.Web.Configuration;
using Flurl;
using Flurl.Http;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace src.Helpers.Api.Authentication.External
{
    public class GoogleExternalProvider : AbstractExternalProvider<ExternalResponseModel, GoogleProfileModel>
    {
        private const string TokenUri = "https://accounts.google.com/o/oauth2/token";
        private const string ProfileUri = "https://www.googleapis.com/plus/v1/people/me/openIdConnect";

        public GoogleExternalProvider()
        { }

        public override async Task<ExternalResponseModel> ExchangeCodeForAccessToken(string code)
        {
            var pathSegments = new
            {
                code = code,
                client_id = WebConfigurationManager.AppSettings["GoogleClientId"],
                client_secret = WebConfigurationManager.AppSettings["GoogleSecret"],
                redirect_uri = WebConfigurationManager.AppSettings["RedirectUri"],
                grant_type = "authorization_code"
            };

            var response = await TokenUri.PostUrlEncodedAsync(pathSegments);

            var content = await response.Content.ReadAsStringAsync();

            var jsonObject = await Task.Factory.StartNew(()=> JsonConvert.DeserializeObject<ExternalResponseModel>(content));

            return jsonObject;
        }

        public override async Task<GoogleProfileModel> GetProfileData(string accessToken)
        {
            var response = await ProfileUri.WithOAuthBearerToken(accessToken).GetAsync();

            var content = await response.Content.ReadAsStringAsync();

            var jsonObject = await Task.Factory.StartNew(() => JsonConvert.DeserializeObject<GoogleProfileModel>(content));

            jsonObject.Picture = jsonObject.Picture.Split(new string[]{ "sz" }, StringSplitOptions.None)[0] + "sz=400";

            return jsonObject;
        }
    }
}