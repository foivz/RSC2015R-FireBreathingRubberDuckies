using src.Models.Abstract;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace src.Models
{
    public class WebRtc: AbstractModel
    {
        [Required]
        public string Url { get; set; }
        [Required]
        public long Team { get; set; }
    }
}