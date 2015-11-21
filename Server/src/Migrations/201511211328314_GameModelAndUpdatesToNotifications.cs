namespace src.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class GameModelAndUpdatesToNotifications : DbMigration
    {
        public override void Up()
        {
            CreateTable(
                "dbo.Games",
                c => new
                    {
                        Id = c.Long(nullable: false, identity: true),
                        Length = c.Int(nullable: false),
                        Finished = c.Boolean(nullable: false),
                        ChallengerOneId = c.Long(),
                        ChallengerTwoId = c.Long(),
                        Created = c.DateTime(),
                        Updated = c.DateTime(),
                        Team_Id = c.Long(),
                    })
                .PrimaryKey(t => t.Id)
                .ForeignKey("dbo.Teams", t => t.ChallengerOneId)
                .ForeignKey("dbo.Teams", t => t.ChallengerTwoId)
                .ForeignKey("dbo.Teams", t => t.Team_Id)
                .Index(t => t.ChallengerOneId)
                .Index(t => t.ChallengerTwoId)
                .Index(t => t.Team_Id);
            
            AddColumn("dbo.Notifications", "OfferUrl", c => c.String(nullable: false));
        }
        
        public override void Down()
        {
            DropForeignKey("dbo.Games", "Team_Id", "dbo.Teams");
            DropForeignKey("dbo.Games", "ChallengerTwoId", "dbo.Teams");
            DropForeignKey("dbo.Games", "ChallengerOneId", "dbo.Teams");
            DropIndex("dbo.Games", new[] { "Team_Id" });
            DropIndex("dbo.Games", new[] { "ChallengerTwoId" });
            DropIndex("dbo.Games", new[] { "ChallengerOneId" });
            DropColumn("dbo.Notifications", "OfferUrl");
            DropTable("dbo.Games");
        }
    }
}
