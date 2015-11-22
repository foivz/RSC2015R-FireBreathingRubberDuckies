namespace src.Migrations
{
    using System;
    using System.Data.Entity.Migrations;
    
    public partial class NFCOnUserModel : DbMigration
    {
        public override void Up()
        {
            AddColumn("dbo.Users", "NFC", c => c.String());
        }
        
        public override void Down()
        {
            DropColumn("dbo.Users", "NFC");
        }
    }
}
