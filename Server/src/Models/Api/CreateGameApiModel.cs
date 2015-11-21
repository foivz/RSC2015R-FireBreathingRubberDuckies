using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.Linq;
using System.Web;

namespace src.Models.Api
{
    public class CreateGameApiModel
    {
        [Required]
        public int Length { get; set; }
        [Required]
        public long ChallengerOne { get; set; }
        [Required]
        public long ChallengerTwo { get; set; }
    }
}