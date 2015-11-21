using System;
using System.Collections.Generic;
using System.Data.Entity;
using System.Linq;
using System.Web;

namespace src.Helpers.Api.Response
{
    public class ApiResponse
    {
        public ApiResponse(int status)
        {
            this.Status = status;
            this.Data = new List<object>();
        }
        public ApiResponse(int status, DbSet<object> data)
            : this(status, data.ToArray())
        { }
        public ApiResponse(int status, params object[] data)
        {
            this.Status = status;
            this.Data = data;
        }

        public int Status { get; set; }
        public IEnumerable<object> Data { get; set; }
    }
}