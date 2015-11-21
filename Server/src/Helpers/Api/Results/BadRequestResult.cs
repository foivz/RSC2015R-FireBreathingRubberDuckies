using src.Helpers.Api.Results.Abstract;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Threading;
using System.Threading.Tasks;
using System.Web;

namespace src.Helpers.Api.Results
{
    public class BadRequestResult : AbstractResult
    {
        public BadRequestResult(HttpRequestMessage request, object data)
            : base(request, data)
        { }
        public override Task<HttpResponseMessage> ExecuteAsync(CancellationToken cancellationToken)
        {
            return Task.FromResult(this.CreateResponse(HttpStatusCode.BadRequest));
        }
    }
}