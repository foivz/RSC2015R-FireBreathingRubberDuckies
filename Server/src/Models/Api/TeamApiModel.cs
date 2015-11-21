using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace src.Models.Api
{
    public class TeamApiModel
    {
        public long Id { get; set; }
        [Required]
        public string Name { get; set; }
        [Required]
        public double Longitude { get; set; }
        [Required]
        public double Latitude { get; set; }
        public ICollection<UserApiModel> Users { get; set; }
    }
}