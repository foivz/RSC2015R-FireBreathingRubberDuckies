using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Http;

namespace src.Helpers.Api.Results
{
    public static class ApiControllerExtensions
    {
        public static IHttpActionResult NotFound(this ApiController controller, object data)
        {
            return new NotFoundResult(controller.Request, data);
        }
        public static IHttpActionResult BadRequest(this ApiController controller, object data)
        {
            return new BadRequestResult(controller.Request, data);
        }
        public static IHttpActionResult Conflict(this ApiController controller, object data)
        {
            return new ConflictResult(controller.Request, data);
        }
        public static IHttpActionResult InternalServerError(this ApiController controller, object data)
        {
            return new InternalServerErrorResult(controller.Request, data);
        }
    }
}