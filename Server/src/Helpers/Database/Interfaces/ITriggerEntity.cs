using EntityFramework.Triggers;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace src.Helpers.Database.Interfaces
{
    public interface ITriggerEntity : ITriggerable
    {
        DateTime? Created { get; set; }
        DateTime? Updated { get; set; }
    }
}
