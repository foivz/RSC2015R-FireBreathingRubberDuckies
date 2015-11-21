using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace src.Helpers.Api.Authentication.External.Models
{
    public class GoogleProfileModel
    {
        public string Sub { get; set; }
        public string Name { get; set; }
        public string Given_Name { get; set; }
        public string Family_Name { get; set; }
        public string Profile { get; set; }
        public string Picture { get; set; }
        public string Email { get; set; }
    }
}