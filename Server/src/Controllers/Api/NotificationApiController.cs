using src.Controllers.Abstract;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web.Http;
using src.Helpers.Api.Results;
using System.Data.Entity;
using AutoMapper;
using src.Helpers.Api.Response;
using src.Models;

namespace src.Controllers.Api
{
    [RoutePrefix("api/1/notifications")]
    public class NotificationApiController : AbstractApiController
    {
        [HttpGet, Route("{number:int}"), Authorize]
        public IHttpActionResult GetTopN(int number)
        {
            var notif = this.db.Notifications.OrderByDescending(x => x.Id).Take(number);

            return this.Ok(new ApiResponse(200, Mapper.Map<IEnumerable<Notification>>(notif).ToArray()));
        }

        [HttpGet, Route(""), Authorize]
        public IHttpActionResult GetAll()
        {
            var notif = this.db.Notifications.OrderByDescending(x => x.Id).ToList();

            var random = new Random(DateTime.Now.Second);

            var list = new List<Notification>();

            while (!list.Count.Equals(notif.Count()))
            {
                var pos = random.Next(0, notif.Count());

                var elem = notif.ElementAt(pos);

                if (!list.Contains(elem))
                {
                    list.Add(elem);
                }
            }

            return this.Ok(new ApiResponse(200, Mapper.Map<IEnumerable<Notification>>(list).ToArray()));
        }

        [HttpGet, Route("single/{id:long}"), Authorize]
        public async Task<IHttpActionResult> GetSingle(long id)
        {
            var wanted = await this.db.Notifications.FindAsync(id);

            if (wanted != null)
                return this.Ok(new ApiResponse(200, wanted));

            return this.NotFound(new ApiResponse(404, id));
        }

        [HttpPost, Route(""), Authorize(Roles = "admin,superadmin")]
        public async Task<IHttpActionResult> Create(Notification model)
        {
            if (ModelState.IsValid && model != null)
            {
                this.db.Notifications.Add(model);

                await this.db.SaveChangesAsync();

                return this.Ok(new ApiResponse(200, model));
            }
            return this.BadRequest(new ApiResponse(400, model));
        }
    }
}
