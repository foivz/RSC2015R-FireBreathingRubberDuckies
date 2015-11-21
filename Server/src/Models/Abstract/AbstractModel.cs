using src.Helpers.Database.Interfaces;
using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;
using System.ComponentModel.DataAnnotations.Schema;
using System.Linq;
using System.Web;
using EntityFramework.Triggers;

namespace src.Models.Abstract
{
    public class AbstractModel: ITriggerEntity
    {
        public AbstractModel()
        {
            this.Triggers().Inserting += (entry) => { entry.Entity.Created = DateTime.Now; };
            this.Triggers().Updating += (entry) => { entry.Entity.Updated = DateTime.Now; };
        }
        [DatabaseGenerated(System.ComponentModel.DataAnnotations.Schema.DatabaseGeneratedOption.Identity), Key]
        public long Id { get; set; }
        public DateTime? Created { get; set; }
        public DateTime? Updated { get; set; }
    }
}