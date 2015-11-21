using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace src.Helpers.Api.AmazonS3.Interfaces
{
    public interface IS3Provider<TFile> where TFile: class
    {
        IS3Provider<TFile> CreateRequest(Stream objectStream, string ext);
        Task<TFile> SaveObject();
    }
}
