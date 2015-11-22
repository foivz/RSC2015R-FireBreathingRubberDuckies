using AutoMapper;
using Microsoft.AspNet.Identity.EntityFramework;
using src.Controllers.Api;
using src.Models;
using src.Models.Api;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;

namespace src.App_Start
{
    public static class AutoMapperConfig
    {
        public static void Configure()
        {
            Mapper.CreateMap<RegisterApiModel, User>().ForMember(x => x.Claims, opts => opts.Ignore()).ForMember(x => x.Logins, opts => opts.Ignore()).ForMember(x => x.Roles, opts => opts.Ignore()).ReverseMap();
            Mapper.CreateMap<CreateUserApiModel, User>().ForMember(x => x.Claims, opts => opts.Ignore()).ForMember(x => x.Logins, opts => opts.Ignore()).ForMember(x => x.Roles, opts => opts.Ignore()).ReverseMap();
            Mapper.CreateMap<RegisterApiModel, UserApiModel>();
            Mapper.CreateMap<User, UserApiModel>().ReverseMap().ForMember(x => x.Claims, opts => opts.Ignore()).ForMember(x => x.Logins, opts => opts.Ignore()).ForMember(x => x.Roles, opts => opts.Ignore());
            Mapper.CreateMap<RoleApiModel, IdentityRole>().ForMember(x => x.Users, opts => opts.Ignore()).ReverseMap();
            Mapper.CreateMap<TeamApiModel, Team>().ReverseMap();
            Mapper.CreateMap<GameApiModel, Game>().ReverseMap();
        }
    }
}