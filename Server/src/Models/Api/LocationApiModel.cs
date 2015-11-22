using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace src.Models.Api
{
    public class LocationApiModel
    {
        [Required]
        public double Long { get; set; }
        [Required]
        public double Lat { get; set; }
        [Required]
        public long MapId { get; set; }
    }
}