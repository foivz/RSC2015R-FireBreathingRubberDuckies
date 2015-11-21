using src.Controllers.Abstract;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Mvc;

namespace src.Controllers
{
    public class AuthController : AbstractController
    {
        [HttpGet, Route("confirm")]
        public async Task<ActionResult> Index(string id, string token)
        {
            if (id != null && token != null)
            {
                await this.userManager.ConfirmEmailAsync(id, token);
            }

            return Redirect("/");
        }
    }
}