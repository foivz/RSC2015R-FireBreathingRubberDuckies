namespace src.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class RTCRoutes : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.WebRtcs",
                c => new
                    {
                        Id = c.Long(nullable: false, identity: true),
                        Url = c.String(nullable: false),
                        Created = c.DateTime(),
                        Updated = c.DateTime(),
                    })
                .PrimaryKey(t => t.Id);
            
        }
        
        public override void Down()
        {
            DropTable("dbo.WebRtcs");
        }
    }
}
