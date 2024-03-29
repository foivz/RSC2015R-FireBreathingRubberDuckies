﻿using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Claims;
using System.Threading.Tasks;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.EntityFramework;
using Microsoft.AspNet.Identity.Owin;
using Microsoft.Owin.Security;
using Microsoft.Owin.Security.Cookies;
using Microsoft.Owin.Security.OAuth;
using src.Models;

namespace src.Providers
{
    public class ApplicationOAuthProvider : OAuthAuthorizationServerProvider
    {
        private readonly string _publicClientId;

        public ApplicationOAuthProvider(string publicClientId)
        {
            if (publicClientId == null)
            {
                throw new ArgumentNullException("publicClientId");
            }

            _publicClientId = publicClientId;
        }

        public override async Task GrantResourceOwnerCredentials(OAuthGrantResourceOwnerCredentialsContext context)
        {
            var userManager = context.OwinContext.GetUserManager<UserManager>();

            var formData = (await context.Request.ReadFormAsync()).FirstOrDefault(x => x.Key.ToLower().Equals("registrationid"));

            string regId = null;

            if (formData.Value != null)
                regId = (formData.Value.First().Equals(string.Empty)) ? null : formData.Value.First();

            User user = await userManager.FindByNameAsync(context.UserName);

            var result = await userManager.CheckPasswordAsync(user, context.Password);

            if (user == null || !user.Enabled || !result || await userManager.IsLockedOutAsync(user.Id))
            {
                if (user != null)
                    if (!result)
                        await userManager.AccessFailedAsync(user.Id);

                context.SetError("invalid_grant", "The user name or password is incorrect.");
                return;
            }

            var role = (await userManager.GetRolesAsync(user.Id)).First();

            if (user.RegistrationId != null)
            {
                if (!user.RegistrationId.Equals(regId))
                    user.RegistrationId = regId;
            }
            else
                user.RegistrationId = regId;

            var idResult = await userManager.UpdateAsync(user);

            ClaimsIdentity oAuthIdentity = await user.GenerateUserIdentityAsync(userManager,
               OAuthDefaults.AuthenticationType);
            ClaimsIdentity cookiesIdentity = await user.GenerateUserIdentityAsync(userManager,
                CookieAuthenticationDefaults.AuthenticationType);

            AuthenticationProperties properties = CreateProperties(user.UserName, role, user.Id);
            AuthenticationTicket ticket = new AuthenticationTicket(oAuthIdentity, properties);
            context.Validated(ticket);
            context.Request.Context.Authentication.SignIn(cookiesIdentity);
        }

        public override Task TokenEndpoint(OAuthTokenEndpointContext context)
        {
            foreach (KeyValuePair<string, string> property in context.Properties.Dictionary)
            {
                context.AdditionalResponseParameters.Add(property.Key, property.Value);
            }

            return Task.FromResult<object>(null);
        }

        public override Task ValidateClientAuthentication(OAuthValidateClientAuthenticationContext context)
        {
            if (context.ClientId == null)
            {
                context.Validated();
            }

            return Task.FromResult<object>(null);
        }

        public override Task ValidateClientRedirectUri(OAuthValidateClientRedirectUriContext context)
        {
            if (context.ClientId == _publicClientId)
            {
                Uri expectedRootUri = new Uri(context.Request.Uri, "/");

                if (expectedRootUri.AbsoluteUri == context.RedirectUri)
                {
                    context.Validated();
                }
            }

            return Task.FromResult<object>(null);
        }

        public static AuthenticationProperties CreateProperties(string userName, string role, string id)
        {
            IDictionary<string, string> data = new Dictionary<string, string>
            {
                { "userName", userName },
                { "role", role },
                { "userId", id }
            };
            return new AuthenticationProperties(data);
        }
    }
}