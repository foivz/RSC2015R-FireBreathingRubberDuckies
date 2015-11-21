using System;
using System.Collections.Generic;
using System.Linq;
using Microsoft.Owin;
using Owin;
using Microsoft.Owin.Cors;
using src.App_Start;

[assembly: OwinStartup(typeof(src.Startup))]

namespace src
{
    public partial class Startup
    {
        public void Configuration(IAppBuilder app)
        {
            app.UseCors(CorsOptions.AllowAll);
            app.MapSignalR();
            ConfigureAuth(app);
            StaticContentConfig.Configure(app);
            AutoMapperConfig.Configure();
        }
    }
}
