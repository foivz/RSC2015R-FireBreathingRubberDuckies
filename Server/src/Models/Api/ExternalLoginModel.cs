using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace src.Models.Api
{
    public class ExternalLoginModel
    {
        public string Code { get; set; }
        public string AccessToken { get; set; }
        public string RegistrationId { get; set; }
    }
}