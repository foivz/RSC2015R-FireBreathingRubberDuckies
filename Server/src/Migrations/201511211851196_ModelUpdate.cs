namespace src.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class ModelUpdate : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.Users", "Banned", c => c.Boolean(nullable: false));
        }
        
        public override void Down()
        {
            DropColumn("dbo.Users", "Banned");
        }
    }
}
