using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading.Tasks;
using System.Web;
using System.Web.Http;

namespace src.Helpers.Api.Results.Abstract
{
    public abstract class AbstractResult : IHttpActionResult
    {
        public AbstractResult(HttpRequestMessage request, object data)
        {
            this.Request = request;
            this.Data = data;
        }
        protected HttpRequestMessage Request { get; set; }
        protected object Data { get; set; }

        protected HttpResponseMessage CreateResponse(HttpStatusCode code)
        {
            return this.Request.CreateResponse(code, this.Data);
        }

        public abstract Task<HttpResponseMessage> ExecuteAsync(System.Threading.CancellationToken cancellationToken);
    }
}