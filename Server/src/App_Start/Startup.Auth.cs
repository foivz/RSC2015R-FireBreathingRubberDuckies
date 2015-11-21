using Microsoft.AspNet.Identity;
using Microsoft.Owin;
using Microsoft.Owin.Security.Cookies;
using Microsoft.Owin.Security.Jwt;
using Microsoft.Owin.Security.OAuth;
using Owin;
using src.Models;
using src.Providers;
using System;

namespace src
{
    public partial class Startup
    {
        public static OAuthAuthorizationServerOptions OAuthOptions { get; private set; }
        public static JwtBearerAuthenticationOptions JwtOptions { get; private set; }
        public static readonly TimeSpan TokenTimeSpan = TimeSpan.FromDays(14);
        public static readonly bool HttpsEnabled = false;
        public static string PublicClientId { get; private set; }

        public void ConfigureAuth(IAppBuilder app)
        {
            app.CreatePerOwinContext(DatabaseContext.Create);
            app.CreatePerOwinContext<UserManager>(UserManager.Create);


            app.UseCookieAuthentication(new CookieAuthenticationOptions());
            app.UseExternalSignInCookie(DefaultAuthenticationTypes.ExternalCookie);

            PublicClientId = "853749170e06bcda85f709b4a5a45c71"; //Also serves as key
            OAuthOptions = new OAuthAuthorizationServerOptions
            {
                TokenEndpointPath = new PathString("/api/1/login"),
                Provider = new ApplicationOAuthProvider(PublicClientId),
                AuthorizeEndpointPath = new PathString("/api/Account/ExternalLogin"),
                AccessTokenExpireTimeSpan = TokenTimeSpan,
                //AllowInsecureHttp = !HttpsEnabled,
                AllowInsecureHttp = true,
                AccessTokenFormat = new JwtFormatProvider(TokenTimeSpan)
            };

            var issuer = "FireBreathingRubberDuckies";
            var audience = "all";
            var key = Convert.FromBase64String("4a3940a482cbe843ce0b6fefb938bf62");
            JwtOptions = new JwtBearerAuthenticationOptions()
            {
                AuthenticationMode = Microsoft.Owin.Security.AuthenticationMode.Active,
                AllowedAudiences = new[] { audience },
                IssuerSecurityTokenProviders = new[]
                { 
                    new SymmetricKeyIssuerSecurityTokenProvider(issuer, key)
                }
            };
            app.UseOAuthAuthorizationServer(OAuthOptions);

            app.UseJwtBearerAuthentication(JwtOptions);

            //app.UseMicrosoftAccountAuthentication(
            //    clientId: "",
            //    clientSecret: "");

            //app.UseTwitterAuthentication(
            //    consumerKey: "",
            //    consumerSecret: "");

            //app.UseFacebookAuthentication(
            //    appId: "",
            //    appSecret: "");

            //app.UseGoogleAuthentication(new GoogleOAuth2AuthenticationOptions()
            //{
            //    ClientId = "",
            //    ClientSecret = ""
            //});
        }
    }
}
