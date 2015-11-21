namespace src.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class KilledFlag : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.Teams", "Latitude", c => c.Double(nullable: false));
            AddColumn("dbo.Teams", "Longitude", c => c.Double(nullable: false));
        }
        
        public override void Down()
        {
            DropColumn("dbo.Teams", "Longitude");
            DropColumn("dbo.Teams", "Latitude");
        }
    }
}
