using Newtonsoft.Json;
using src.Models.Abstract;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;

namespace src.Models
{
    public class Map : AbstractModel
    {
        public Map()
        {
            this.Coordinates = new HashSet<Coords>();
            this.Games = new HashSet<Game>();
        }
        public virtual ICollection<Coords> Coordinates { get; set; }
        [JsonIgnore]
        public virtual ICollection<Game> Games { get; set; }
    }
}