using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace src.Helpers.PushNotifications
{
    public class PushNotificationData
    {
        public string Message { get; set; }
        public int Action { get; set; }
        public object Data { get; set; }
    }
}