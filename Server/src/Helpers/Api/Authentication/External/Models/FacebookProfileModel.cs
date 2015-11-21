using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace src.Helpers.Api.Authentication.External.Models
{
    public class FacebookProfileModel
    {
        public string Id { get; set; }
        public string Name { get; set; }
        public string First_Name { get; set; }
        public string Last_Name { get; set; }
        public string Link { get; set; }
        public FacebookPictureInformation Picture { get; set; }
        public string Email { get; set; }
    }

    public class FacebookPictureInformation
    {
        public FacebookPictureInternal Data { get; set; }
    }

    public class FacebookPictureInternal
    {
        public bool Is_Silhouette { get; set; }
        public string Url { get; set; }
    }
}