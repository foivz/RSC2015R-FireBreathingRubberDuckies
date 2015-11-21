using src.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;
using Microsoft.Owin;
using Microsoft.AspNet.Identity.Owin;
using Microsoft.AspNet.Identity.EntityFramework;
using System.Web.Mvc;
using System.Threading.Tasks;
using Microsoft.AspNet.Identity;

namespace src.Controllers.Abstract
{
    public abstract class AbstractApiController : ApiController
    {
        public DatabaseContext db
        {
            get
            {
                return this.Request.GetOwinContext().Get<DatabaseContext>();
            }
        }

        public UserManager userManager
        {
            get
            {
                return this.Request.GetOwinContext().GetUserManager<UserManager>();
            }
        }

        public User CurrentUser
        {
            get
            {
                return this.GetCurrentRequestUser();
            }
        }

        public string UserId
        {
            get
            {
                return this.User.Identity.GetUserId();
            }
        }

        private User GetCurrentRequestUser()
        {
            string userId = this.User.Identity.GetUserId();
            if (userId != null)
                return this.userManager.FindByIdAsync(userId).Result;
            return null;
        }
    }
}