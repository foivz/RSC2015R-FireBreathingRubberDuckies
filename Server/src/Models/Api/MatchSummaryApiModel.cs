using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace src.Models.Api
{
    public class MatchSummaryApiModel
    {
        public int Length { get; set; }
        public TeamApiModel ChallengerOne { get; set; }
        public TeamApiModel ChallengerTwo { get; set; }
        public int TotalKills { get; set; }
    }
}