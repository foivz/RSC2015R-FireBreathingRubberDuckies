namespace src.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class KilledProperty : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.Users", "Killed", c => c.Boolean(nullable: false));
        }
        
        public override void Down()
        {
            DropColumn("dbo.Users", "Killed");
        }
    }
}
