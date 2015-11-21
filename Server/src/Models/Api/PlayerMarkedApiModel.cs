using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace src.Models.Api
{
    public class PlayerMarkedApiModel
    {
        [Required]
        public string NFC { get; set; }
    }
}