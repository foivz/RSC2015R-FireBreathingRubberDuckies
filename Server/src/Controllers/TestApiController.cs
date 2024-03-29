﻿using src.Controllers.Abstract;
using src.Helpers.Api.AmazonS3;
using src.Helpers.Api.AmazonS3.Interfaces;
using src.Helpers.Api.AmazonS3.Models;
using src.Helpers.Api.Hubs;
using src.Helpers.Api.Response;
using src.Helpers.Email;
using src.Helpers.PushNotifications;
using src.Models;
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
using src.Helpers.Api.Results;
using System.Threading;

namespace src.Controllers
{
    [RoutePrefix("api/1/test")]
    public class TestApiController : AbstractApiController
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

            //new GcmProvider().CreateNotification(notification, registrationId).SendAsync().Wait();

            new GcmProvider().CreateNotification(new PushNotificationData
            {
                Action = 2,
                Message = "Added to the team!",
                Data = new
                {
                    GameId = 1
                }
            }, "APA91bFeh8G-XN6hi1ljiX5AKLjLfOI4wWqwGQl-zNCuejn-hFyhd8_YJC7kFY-1zQwk_Qj_yQwZNnbzkT7noSLRr_JUUVkGifyzGj5C1cb0bAebbfBadHkKftu973RfTWeNReQV2M_I").SendAsync().Wait();

            Thread.Sleep(100);

            new GcmProvider().CreateNotification(new PushNotificationData
            {
                Action = 10,
                Message = "Added to the team!",
                Data = new
                {
                    Url = this.db.RTCs.First().Url
                }
            }, "APA91bFeh8G-XN6hi1ljiX5AKLjLfOI4wWqwGQl-zNCuejn-hFyhd8_YJC7kFY-1zQwk_Qj_yQwZNnbzkT7noSLRr_JUUVkGifyzGj5C1cb0bAebbfBadHkKftu973RfTWeNReQV2M_I").SendAsync().Wait();  

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

        [HttpGet, Route("webrtc")]
        public async Task<IHttpActionResult> WebRTCUrl()
        {
            var rtc = this.db.RTCs.FirstOrDefault();

            return this.Ok(new ApiResponse(200, rtc));
        }

        [HttpPost, Route("webrtc")]
        public async Task<IHttpActionResult> WebRTC(WebRtc model)
        {
            if(ModelState.IsValid && model != null)
            {
                //this.db.RTCs.RemoveRange(this.db.RTCs.ToList());

                //await this.db.SaveChangesAsync();

                this.db.RTCs.Add(model);

                await this.db.SaveChangesAsync();

                var team = this.db.Users.ToList();

                foreach (var user in team)
                {
                    new GcmProvider().CreateNotification(new PushNotificationData
                    {
                        Action = 10,
                        Message = "Audio chat!",
                        Data = new
                        {
                            Url = model.Url
                        }
                    }, user.RegistrationId).SendAsync().Wait();
                }

                return this.Ok(new ApiResponse(200, model));
            }
            return this.BadRequest(new ApiResponse(400, model));
        }
    }
}
