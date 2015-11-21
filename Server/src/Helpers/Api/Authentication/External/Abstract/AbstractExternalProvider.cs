using src.Helpers.Api.Authentication.External.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Web;

namespace src.Helpers.Api.Authentication.External.Abstract
{
    public abstract class AbstractExternalProvider<TToken, TProfile> : IExternalProvider<TToken, TProfile>
        where TToken : class
        where TProfile : class
    {
        protected string ClientId { get; set; }
        protected string Secret { get; set; }
        protected string RedirectUri { get; set; }

        public AbstractExternalProvider()
        { }

        public abstract Task<TToken> ExchangeCodeForAccessToken(string code);
        public abstract Task<TProfile> GetProfileData(string accessToken);
    }
}