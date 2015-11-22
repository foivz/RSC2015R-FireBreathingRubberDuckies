using src.Controllers.Abstract;
using src.Helpers.Api.Response;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web.Http;
using src.Helpers.Api.Results;
using src.Models.Api;
using AutoMapper;
using src.Models;
using System.Data.Entity;
using src.Helpers.PushNotifications;

namespace src.Controllers.Api
{
    [RoutePrefix("api/1/teams")]
    public class TeamApiController : AbstractApiController
    {
        public TeamApiController()
        {

        }

        [HttpGet, Route(""), Authorize]
        public async Task<IHttpActionResult> GetAll()
        {
            var teams = this.db.Teams;
            return this.Ok(new ApiResponse(200, Mapper.Map<IEnumerable<TeamApiModel>>(teams).ToArray()));
        }

        [HttpGet, Route("{id:long}"), Authorize]
        public async Task<IHttpActionResult> GetTeams(long id)
        {
            var wanted = await this.db.Teams.FindAsync(id);
            if (wanted != null)
                return this.Ok(new ApiResponse(200, wanted));
            return this.NotFound(new ApiResponse(404, id));
        }

        [HttpPost, Route(""), Authorize(Roles = "admin,superadmin")]
        public async Task<IHttpActionResult> Create(TeamApiModel model)
        {
            if (ModelState.IsValid && model != null)
            {
                var mapped = Mapper.Map<Team>(model);

                this.db.Teams.Add(mapped);

                await this.db.SaveChangesAsync();

                return this.Ok(new ApiResponse(200, Mapper.Map(mapped, model)));
            }

            return this.BadRequest(new ApiResponse(400, model));
        }

        [HttpPost, Route("add"), Authorize(Roles = "admin,superadmin")]
        public async Task<IHttpActionResult> AddTeamMember(AddMemberApiModel model)
        {
            if (ModelState.IsValid && model != null)
            {
                var team = await this.db.Teams.FindAsync(model.TeamId);

                var member = await this.userManager.FindByIdAsync(model.UserId);

                if (!team.Users.Contains(member) && await this.userManager.IsInRoleAsync(model.UserId, "user"))
                {
                    member.Killed = false;
                    member.Banned = false;
                    member.NFC = null;

                    team.Users.Add(member);

                    await this.db.SaveChangesAsync();

                    new GcmProvider().CreateNotification(new PushNotificationData
                    {
                        Action = 1,
                        Message = "Added to the team!",
                        Data = new
                        {
                            TeamId = team.Id
                        }
                    }, member.RegistrationId).SendAsync().Wait();

                    return this.Ok(new ApiResponse(200, model));
                }
                return this.BadRequest(new ApiResponse(400, model));
            }
            return this.BadRequest(new ApiResponse(400, model));
        }

        [HttpPost, Route("addmultiple"), Authorize(Roles = "admin,superadmin")]
        public async Task<IHttpActionResult> AddTeamMember(AddMemberApiModel[] array)
        {
            if (ModelState.IsValid && array != null)
            {
                foreach (var model in array)
                {
                    var team = await this.db.Teams.FindAsync(model.TeamId);

                    var member = await this.userManager.FindByIdAsync(model.UserId);

                    if (!team.Users.Contains(member) && await this.userManager.IsInRoleAsync(model.UserId, "user"))
                    {
                        member.Killed = false;
                        member.Banned = false;
                        member.NFC = null;

                        team.Users.Add(member);

                        await this.db.SaveChangesAsync();

                        new GcmProvider().CreateNotification(new PushNotificationData
                        {
                            Action = 8,
                            Message = "Added to the team!",
                            Data = new
                            {
                                TeamId = team.Id
                            }
                        }, member.RegistrationId).SendAsync().Wait();  
                    }
                }
                return this.Ok(new ApiResponse(200));
            }
            return this.BadRequest(new ApiResponse(400));
        }
    }
}
