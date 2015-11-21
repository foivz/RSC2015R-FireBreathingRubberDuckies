using Microsoft.Owin.Security;
using Microsoft.Owin.Security.OAuth;
using src.Helpers.Api.Authentication.Tokens.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Web;

namespace src.Helpers.Api.Authentication.Tokens
{
    public class TokenBuilder : ITokenBuilder
    {
        public ClaimsIdentity Identity { get; set; }
        public TokenBuilder(string userId, string username, string role)
        {
            this.Identity = new ClaimsIdentity(OAuthDefaults.AuthenticationType);
            Identity.AddClaim(new Claim(ClaimTypes.Name, username));
            Identity.AddClaim(new Claim("role", role));
            Identity.AddClaim(new Claim(ClaimTypes.NameIdentifier, userId));
        }

        public AuthenticationProperties BuildAuthenticationProperties()
        {
            return new AuthenticationProperties{
                IssuedUtc = DateTime.UtcNow,
                ExpiresUtc = DateTime.UtcNow.Add(Startup.TokenTimeSpan)
            };
        }

        public string GetLocalAccessToken()
        {
            return Startup.OAuthOptions.AccessTokenFormat.Protect(new AuthenticationTicket(this.Identity, this.BuildAuthenticationProperties()));
        }
    }
}