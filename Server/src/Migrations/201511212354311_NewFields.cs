namespace src.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class NewFields : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.Games", "Start", c => c.DateTime());
            AddColumn("dbo.Games", "End", c => c.DateTime());
        }
        
        public override void Down()
        {
            DropColumn("dbo.Games", "End");
            DropColumn("dbo.Games", "Start");
        }
    }
}
