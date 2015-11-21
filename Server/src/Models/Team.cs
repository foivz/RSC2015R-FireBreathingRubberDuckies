using src.Models.Abstract;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace src.Models
{
    public class Team : AbstractModel
    {
        public Team()
        {
            this.Users = new HashSet<User>();
            this.Games = new HashSet<Game>();
        }
        public string Name { get; set; }
        public virtual ICollection<User> Users { get; set; }
        public virtual ICollection<Game> Games { get; set; }
    }
}