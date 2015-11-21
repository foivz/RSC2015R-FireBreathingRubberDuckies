namespace src.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class MapsAddedToTheGame : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.Maps",
                c => new
                    {
                        Id = c.Long(nullable: false, identity: true),
                        Created = c.DateTime(),
                        Updated = c.DateTime(),
                    })
                .PrimaryKey(t => t.Id);
            
            CreateTable(
                "dbo.Coords",
                c => new
                    {
                        Id = c.Long(nullable: false, identity: true),
                        Lat = c.Double(nullable: false),
                        Lng = c.Double(nullable: false),
                        MapId = c.Long(),
                        Created = c.DateTime(),
                        Updated = c.DateTime(),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Maps", t => t.MapId)
                .Index(t => t.MapId);
            
            AddColumn("dbo.Games", "MapId", c => c.Long());
            CreateIndex("dbo.Games", "MapId");
            AddForeignKey("dbo.Games", "MapId", "dbo.Maps", "Id");
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.Games", "MapId", "dbo.Maps");
            DropForeignKey("dbo.Coords", "MapId", "dbo.Maps");
            DropIndex("dbo.Coords", new[] { "MapId" });
            DropIndex("dbo.Games", new[] { "MapId" });
            DropColumn("dbo.Games", "MapId");
            DropTable("dbo.Coords");
            DropTable("dbo.Maps");
        }
    }
}
