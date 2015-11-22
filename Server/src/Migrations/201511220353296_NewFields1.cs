namespace src.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class NewFields1 : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.WebRtcs", "Team", c => c.Long(nullable: false));
        }
        
        public override void Down()
        {
            DropColumn("dbo.WebRtcs", "Team");
        }
    }
}
