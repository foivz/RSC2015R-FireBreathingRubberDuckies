using src.Helpers.Api.AmazonS3;
using src.Helpers.Api.AmazonS3.Interfaces;
using src.Helpers.Api.AmazonS3.Models;
using src.Helpers.Api.Hubs;
using src.Helpers.Api.Response;
using src.Helpers.Email;
using src.Helpers.PushNotifications;
using src.Models.Api;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web;
using System.Web.Configuration;
using System.Web.Http;

namespace src.Controllers
{
    [RoutePrefix("api/1/test")]
    public class TestApiController : ApiController
    {
        [HttpGet, Route("gcm/{registrationId}")]
        public IHttpActionResult Test(string registrationId)
        {
            var notification = new PushNotificationData
            {
                Action = 1,
                Message = "Test notification",
                Data = new
                {
                    payload = "Test payload"
                }
            };

            new GcmProvider().CreateNotification(notification, registrationId).SendAsync().Wait();

            return Ok(new ApiResponse(200, notification));
        }

        [HttpPost, Route("email")]
        public IHttpActionResult Email(TestEmailModel model)
        {
            var client = new EmailProvider();

            client.SendMail(WebConfigurationManager.AppSettings["AppEmail"], model.Email, "Test", "Test email!");

            return Ok(new ApiResponse(200, new { Email = model.Email }));
        }

        [HttpGet, Route("amazon")]
        public async Task<IHttpActionResult> S3UploadTest()
        {
            IS3Provider<S3ApiModel> amazon = new S3Client();

            Stream testStream = new MemoryStream();

            byte[] data = File.ReadAllBytes(HttpContext.Current.Server.MapPath("~/test.pdf"));

            await testStream.WriteAsync(data, 0, data.Count());

            var obj = await amazon.CreateRequest(testStream, ".pdf").SaveObject();

            return this.Ok(new ApiResponse(200, obj));
        }
    }
}
