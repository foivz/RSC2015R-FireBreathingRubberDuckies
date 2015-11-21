using Newtonsoft.Json;
using src.Models.Abstract;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace src.Models
{
    public class Coords: AbstractModel
    {
        public double Lat { get; set; }
        public double Lng { get; set; }

        public long? MapId { get; set; }
        [ForeignKey("MapId"), JsonIgnore]
        public virtual Map Map { get; set; }
    }
}