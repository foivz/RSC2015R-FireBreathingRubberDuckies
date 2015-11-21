using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace src.Helpers.Api.Authentication.External.Models
{
    public class ExternalResponseModel
    {
        public string Access_Token { get; set; }
        public int Expires_In { get; set; }
        public string Token_Type { get; set; }
    }
}