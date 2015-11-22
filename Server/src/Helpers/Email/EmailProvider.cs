using Microsoft.AspNet.Identity;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net.Mail;
using System.Threading.Tasks;
using System.Web;
using System.Web.Configuration;
using Typesafe.Mailgun;

namespace src.Helpers.Email
{
    public class EmailProvider : IIdentityMessageService
    {
        private MailgunClient client { get; set; }
        public EmailProvider()
        {
            this.client = new MailgunClient(WebConfigurationManager.AppSettings["MailgunDomain"], WebConfigurationManager.AppSettings["MailgunKey"]);
        }
        public bool SendMail(string from, string to, string subject, string body)
        {
            try
            {
                var mailMessage = new MailMessage(from, to, subject, body);
                this.client.SendMail(mailMessage);
                return true;
            }
            catch (Exception)
            { }
            return false;
        }
        public Task SendAsync(IdentityMessage message)
        {
            var mailMessage = new MailMessage(WebConfigurationManager.AppSettings["AppEmail"], message.Destination, message.Subject, message.Body);
            //this.client.SendMail(mailMessage);
            return Task.FromResult(this.client.SendMail(mailMessage));
        }
    }
}