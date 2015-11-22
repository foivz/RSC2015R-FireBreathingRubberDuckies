using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace src.Helpers.Api.Authentication.External.Interfaces
{
    public interface IExternalProvider<TToken, TProfile>
        where TToken : class
        where TProfile : class
    {
        Task<TToken> ExchangeCodeForAccessToken(string code);
        Task<TProfile> GetProfileData(string accessToken);
    }
}
