namespace src.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class StartedAttribute : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.Games", "Started", c => c.Boolean(nullable: false));
        }
        
        public override void Down()
        {
            DropColumn("dbo.Games", "Started");
        }
    }
}
