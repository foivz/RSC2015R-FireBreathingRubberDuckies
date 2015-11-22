using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Microsoft.AspNet.Identity.Owin;

namespace src.Controllers.Abstract
{
    public abstract class AbstractController : Controller
    {
        public UserManager userManager
        {
            get
            {
                return this.HttpContext.GetOwinContext().GetUserManager<UserManager>();
            }
        }
    }
}