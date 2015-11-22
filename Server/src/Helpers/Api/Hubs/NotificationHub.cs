using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using Microsoft.AspNet.SignalR;

namespace src.Helpers.Api.Hubs
{
    public class NotificationHub : Hub
    {
        public void Message()
        {
            Clients.All.hello("Hello");
        }

        public static IHubContext GetHubContext()
        {
            return GlobalHost.ConnectionManager.GetHubContext<NotificationHub>();
        }
    }
}