using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace src.Models
{
    public class WebRtc
    {
        [Required]
        public string Url { get; set; }
    }
}