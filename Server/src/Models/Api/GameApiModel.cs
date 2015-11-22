using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace src.Models.Api
{
    public class GameApiModel
    {
        public long Id { get; set; }
        public int Length { get; set; }
        public bool Started { get; set; }
        public DateTime? Start { get; set; }
        public DateTime? End { get; set; }
        public bool Finished { get; set; }
        public TeamApiModel ChallengerOne { get; set; }
        public TeamApiModel ChallengerTwo { get; set; }
        public long? MapId { get; set; }
    }
}