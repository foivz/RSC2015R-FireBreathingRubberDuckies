using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace src.Models.Api
{
    public class AddMemberApiModel
    {
        [Required]
        public string UserId { get; set; }
        [Required]
        public long TeamId { get; set; }
    }
}