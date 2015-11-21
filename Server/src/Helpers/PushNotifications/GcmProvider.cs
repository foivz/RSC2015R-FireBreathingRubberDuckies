using Newtonsoft.Json;
using Newtonsoft.Json.Serialization;
using PushSharp.Android;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Configuration;

namespace src.Helpers.PushNotifications
{
    public class GcmProvider
    {
        private const string GCM = "https://android.googleapis.com/gcm/send";
        private static string ApiKey { get; set; }
        private GcmNotification Notification { get; set; }

        public GcmProvider()
        {
            ApiKey = WebConfigurationManager.AppSettings["GcmApiKey"];
            this.Notification = new GcmNotification();
        }

        public GcmProvider CreateNotification(object data, params string[] registrationIds)
        {
            GcmNotification notification = new GcmNotification { NotificationKey = ApiKey };

            foreach (string id in registrationIds)
                notification.RegistrationIds.Add(id);

            notification.JsonData = JsonConvert.SerializeObject(data, new JsonSerializerSettings
            {
                ContractResolver = new CamelCasePropertyNamesContractResolver()
            });

            this.Notification = notification;
            return this;
        }

        public Task SendAsync()
        {
            using (GcmPushChannel channel = new GcmPushChannel(new GcmPushChannelSettings(ApiKey) { GcmUrl = GCM }))
                return Task.Factory.StartNew(() => channel.SendNotification(this.Notification, null));
        }
    }
}