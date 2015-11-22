namespace src.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class Add : DbMigration
    {
        public override void Up()
        {
            DropColumn("dbo.WebRtcs", "Team");
        }
        
        public override void Down()
        {
            AddColumn("dbo.WebRtcs", "Team", c => c.Long(nullable: false));
        }
    }
}
