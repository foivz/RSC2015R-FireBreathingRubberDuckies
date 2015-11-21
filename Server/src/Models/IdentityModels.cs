using System.Security.Claims;
using System.Threading.Tasks;
using Microsoft.AspNet.Identity;
using Microsoft.AspNet.Identity.EntityFramework;
using Microsoft.AspNet.Identity.Owin;
using System;
using EntityFramework.Triggers;
using System.Threading;
using src.Helpers.Database.Interfaces;
using System.Data.Entity;
using System.Collections;
using System.Collections.Generic;

namespace src.Models
{
    public class User : IdentityUser, ITriggerEntity
    {
        public string FirstName { get; set; }
        public string LastName { get; set; }
        public double Latitude { get; set; }
        public double Longitude { get; set; }
        public string RegistrationId { get; set; }
        public string Avatar { get; set; }
        public bool Killed { get; set; }
        public bool Banned { get; set; }
        public bool Enabled { get; set; }
        public string NFC { get; set; }
        public User()
        {
            this.Triggers().Inserting += (entry) => { entry.Entity.Created = DateTime.Now; };
            this.Triggers().Updating += (entry) => { entry.Entity.Updated = DateTime.Now; };
            this.Teams = new HashSet<Team>();
        }
        public async Task<ClaimsIdentity> GenerateUserIdentityAsync(UserManager<User> manager, string authenticationType)
        {
            var userIdentity = await manager.CreateIdentityAsync(this, authenticationType);

            return userIdentity;
        }

        public ICollection<Team> Teams { get; set; }

        public DateTime? Created { get; set; }
        public DateTime? Updated { get; set; }
    }

    public class DatabaseContext : IdentityDbContext<User>
    {
        public virtual DbSet<Team> Teams { get; set; }
        public virtual DbSet<Notification> Notifications { get; set; }
        public virtual DbSet<Game> Games { get; set; }
        public virtual DbSet<WebRtc> RTCs { get; set; }
        public DatabaseContext()
            : base("FBRDOnline", throwIfV1Schema: false)
        {
        }

        public static DatabaseContext Create()
        {
            return new DatabaseContext();
        }

        protected override void OnModelCreating(System.Data.Entity.DbModelBuilder modelBuilder)
        {
            base.OnModelCreating(modelBuilder);

            modelBuilder.Entity<User>().ToTable("Users");
            modelBuilder.Entity<IdentityUserRole>().ToTable("UserRoles");
            modelBuilder.Entity<IdentityUserLogin>().ToTable("UserLogins");
            modelBuilder.Entity<IdentityUserClaim>().ToTable("UserClaims");
            modelBuilder.Entity<IdentityRole>().ToTable("Roles");
        }

        public override Int32 SaveChanges()
        {
            return this.SaveChangesWithTriggers();
        }
        public override Task<Int32> SaveChangesAsync(CancellationToken cancellationToken)
        {
            return this.SaveChangesWithTriggersAsync(cancellationToken);
        }
    }
}