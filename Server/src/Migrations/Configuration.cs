namespace src.Migrations
{
    using Microsoft.AspNet.Identity.EntityFramework;
    using src.Models;
    using System;
    using System.Data.Entity;
    using System.Data.Entity.Migrations;
    using System.Data.Entity.Validation;
    using System.Linq;
    using System.Text;

    internal sealed class Configuration : DbMigrationsConfiguration<src.Models.DatabaseContext>
    {
        public Configuration()
        {
            AutomaticMigrationsEnabled = false;
            AutomaticMigrationDataLossAllowed = true;
        }

        protected override void Seed(src.Models.DatabaseContext context)
        {
            var userMan = new UserManager(new UserStore<User>(context));

            userMan.UserLockoutEnabledByDefault = true;

            context.Roles.Add(new IdentityRole
            {
                Name = "superadmin"
            });

            context.Roles.Add(new IdentityRole
            {
                Name = "admin"
            });

            context.Roles.Add(new IdentityRole
            {
                Name = "user"
            });

            context.SaveChanges();

            var su = new User
            {
                FirstName = FakeO.Name.First(),
                LastName = FakeO.Name.Last(),
                UserName = "super",
                Email = "super@super.com",
                EmailConfirmed = true,
                Enabled = true
            };

            userMan.CreateAsync(su, "123456").Wait();

            userMan.AddToRoleAsync(su.Id, "superadmin").Wait();

            var admin = new User
            {
                FirstName = FakeO.Name.First(),
                LastName = FakeO.Name.Last(),
                UserName = "admin",
                Email = "admin@admin.com",
                EmailConfirmed = true,
                Enabled = true
            };

            userMan.CreateAsync(admin, "123456").Wait();

            userMan.AddToRoleAsync(admin.Id, "admin").Wait();

            var user = new User
            {
                FirstName = FakeO.Name.First(),
                LastName = FakeO.Name.Last(),
                UserName = "user",
                Email = "user@user.com",
                EmailConfirmed = true,
                Enabled = true
            };

            userMan.CreateAsync(user, "123456").Wait();

            userMan.AddToRoleAsync(user.Id, "user").Wait();
        }

        private void SaveChanges(DbContext context)
        {
            try
            {
                context.SaveChanges();
            }
            catch (DbEntityValidationException ex)
            {
                StringBuilder sb = new StringBuilder();

                foreach (var failure in ex.EntityValidationErrors)
                {
                    sb.AppendFormat("{0} failed validation\n", failure.Entry.Entity.GetType());
                    foreach (var error in failure.ValidationErrors)
                    {
                        sb.AppendFormat("- {0} : {1}", error.PropertyName, error.ErrorMessage);
                        sb.AppendLine();
                    }
                }

                throw new DbEntityValidationException(
                    "Entity Validation Failed - errors follow:\n" +
                    sb.ToString(), ex
                );
            }
        }
    }
}
