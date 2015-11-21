using Microsoft.AspNet.Identity.EntityFramework;
using Microsoft.Owin.Security;
using Microsoft.Owin.Security.OAuth;
using src.Models;
using System;
using System.Collections.Generic;
using System.IdentityModel.Tokens;
using System.Linq;
using System.Net.Http;
using System.Security.Principal;
using System.Threading;
using System.Threading.Tasks;
using System.Web;

namespace src.Helpers.Api.Handlers
{
    public class WebApiHandler : DelegatingHandler
    {
        protected async override System.Threading.Tasks.Task<HttpResponseMessage> SendAsync(HttpRequestMessage request, System.Threading.CancellationToken cancellationToken)
        {
            var query = request.RequestUri.ParseQueryString();

            if (query.AllKeys.Any(x => x == "token"))
            {
                var jwt = ((JwtSecurityToken)new JwtSecurityTokenHandler().ReadToken(query.GetValues("token")[0]));

                var userId = jwt.Claims.FirstOrDefault(x => x.Type.Equals("nameid")).Value;

                if (userId != null)
                    using (var context = DatabaseContext.Create())
                    using (var userManager = new UserManager(new UserStore<User>(context)))
                    {
                        User currentUser = null;

                        if ((currentUser = await userManager.FindByIdAsync(userId)) != null)
                        {
                            var identity = await currentUser.GenerateUserIdentityAsync(userManager, OAuthDefaults.AuthenticationType);

                            var principal = new GenericPrincipal(identity, (await userManager.GetRolesAsync(currentUser.Id)).ToArray());

                            Thread.CurrentPrincipal = principal;
                            HttpContext.Current.User = principal;
                        }
                    }
            }

            return await base.SendAsync(request, cancellationToken);
        }
    }
}