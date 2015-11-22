using Microsoft.Owin.Security;
using Microsoft.Owin.Security.OAuth;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens;
using System.Linq;
using System.Web;

namespace src.Providers
{
    public class JwtFormatProvider : ISecureDataFormat<AuthenticationTicket>
    {
        private TimeSpan span { get; set; }
        public JwtFormatProvider(TimeSpan span)
        {
            this.span = span;
        }

        public string SignatureAlgorithm
        {
            get { return "http://www.w3.org/2001/04/xmldsig-more#hmac-sha256"; }
        }

        public string DigestAlgorithm
        {
            get { return "http://www.w3.org/2001/04/xmlenc#sha256"; }
        }

        public string Protect(AuthenticationTicket data)
        {
            if (data == null) throw new ArgumentNullException("data");

            var issuer = "FireBreathingRubberDuckies";
            var audience = "all";
            var key = Convert.FromBase64String("4a3940a482cbe843ce0b6fefb938bf62");
            var now = DateTime.UtcNow;
            var expires = now.AddMinutes(span.TotalMinutes);
            var signingCredentials = new SigningCredentials(
                                        new InMemorySymmetricSecurityKey(key),
                                        SignatureAlgorithm,
                                        DigestAlgorithm);
            var token = new JwtSecurityToken(issuer, audience, data.Identity.Claims,
                                             now, expires, signingCredentials);

            return new JwtSecurityTokenHandler().WriteToken(token);
        }

        public AuthenticationTicket Unprotect(string protectedText)
        {
            throw new NotImplementedException();
        }
    }
}