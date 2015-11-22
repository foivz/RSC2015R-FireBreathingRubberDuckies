using src.Models.Abstract;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace src.Models
{
    public class Game: AbstractModel
    {
        public int Length { get; set; }
        public bool Started { get; set; }
        public DateTime? Start { get; set; }
        public DateTime? End { get; set; }
        public bool Finished { get; set; }
        public long? ChallengerOneId { get; set; }
        [ForeignKey("ChallengerOneId")]
        public Team ChallengerOne { get; set; }
        public long? ChallengerTwoId { get; set; }
        [ForeignKey("ChallengerTwoId")]
        public Team ChallengerTwo { get; set; }
        public long? MapId { get; set; }
        [ForeignKey("MapId")]
        public virtual Map Map { get; set; }
    }
}