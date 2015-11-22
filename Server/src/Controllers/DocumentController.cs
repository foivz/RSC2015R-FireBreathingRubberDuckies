using src.Models;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Web;
using System.Web.Mvc;
using Microsoft.AspNet.Identity.EntityFramework;
using Microsoft.AspNet.Identity.Owin;
using System.Threading.Tasks;
using src.Helpers.Api.AmazonS3.Models;
using src.Helpers.Api.AmazonS3.Interfaces;
using src.Helpers.Api.AmazonS3;
using src.Helpers.Api.Pdf;
using System.Data.Entity;
using src.Helpers.Api.Response;

namespace src.Controllers
{
    public class DocumentController : Controller
    {
        private DatabaseContext db
        {
            get
            {
                return this.HttpContext.GetOwinContext().Get<DatabaseContext>();
            }
        }

        [HttpGet, Route("document/{id:long}")]
        public async Task<JsonResult> Index(long id)
        {
            var game = await this.db.Games.Where(x => x.Id.Equals(id)).Include(x => x.ChallengerOne).Include(x => x.ChallengerTwo).FirstAsync();

            IS3Provider<S3ApiModel> amazon = new S3Client();

            Stream testStream = new MemoryStream();

            Pdf.CreateDocument(this.GetViewHtml(game, "_pdfPartial"), testStream);

            var obj = await amazon.CreateRequest(testStream, ".pdf").SaveObject();

            return Json(new ApiResponse(200, obj), JsonRequestBehavior.AllowGet);
        }

        protected string GetViewHtml(object model, string viewName)
        {
            ViewData.Model = model;
            using (var sw = new StringWriter())
            {
                var viewResult = ViewEngines.Engines.FindPartialView(ControllerContext,
                                                                         viewName);
                var viewContext = new ViewContext(ControllerContext, viewResult.View,
                                             ViewData, TempData, sw);
                viewResult.View.Render(viewContext, sw);
                viewResult.ViewEngine.ReleaseView(ControllerContext, viewResult.View);
                return sw.GetStringBuilder().ToString();
            }
        }
    }
}