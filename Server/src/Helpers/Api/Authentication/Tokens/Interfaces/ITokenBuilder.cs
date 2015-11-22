using Microsoft.Owin.Security;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Text;
using System.Threading.Tasks;

namespace src.Helpers.Api.Authentication.Tokens.Interfaces
{
    public interface ITokenBuilder
    {
        ClaimsIdentity Identity { get; set; }
        AuthenticationProperties BuildAuthenticationProperties();
        string GetLocalAccessToken();
    }
}
