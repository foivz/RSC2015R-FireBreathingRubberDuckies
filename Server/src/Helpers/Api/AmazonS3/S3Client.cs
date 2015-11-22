using Amazon;
using Amazon.S3;
using Amazon.S3.Model;
using src.Helpers.Api.AmazonS3.Interfaces;
using src.Helpers.Api.AmazonS3.Models;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Threading.Tasks;
using System.Web;
using System.Web.Configuration;

namespace src.Helpers.Api.AmazonS3
{
    public class S3Client : IS3Provider<S3ApiModel>, IDisposable
    {
        private string accessKey { get; set; }
        private string secretKey { get; set; }
        private string bucketName { get; set; }
        private string genFileKey { get; set; }
        private AmazonS3Client client { get; set; }
        private PutObjectRequest request { get; set; }
        public S3Client()
            : this(WebConfigurationManager.AppSettings["S3AccessKey"], WebConfigurationManager.AppSettings["S3SecretKey"], WebConfigurationManager.AppSettings["S3Bucket"])
        { }
        public S3Client(string accessKey, string secretKey, string bucket)
        {
            this.accessKey = accessKey;
            this.secretKey = secretKey;
            this.bucketName = bucket;
        }
        public IS3Provider<S3ApiModel> CreateRequest(Stream objectStream, string ext)
        {
            this.genFileKey = string.Format("{0}.{1}", FakeO.Distinct.String(), ext);

            this.request = new PutObjectRequest
            {
                BucketName = this.bucketName,
                Key = this.genFileKey,
                InputStream = objectStream
            };

            return this;
        }

        public async Task<S3ApiModel> SaveObject()
        {
            var endpoint = new AmazonS3Config
            {
                RegionEndpoint = RegionEndpoint.EUWest1
            };

            this.client = new AmazonS3Client(this.accessKey, this.secretKey, endpoint);

            var result = await client.PutObjectAsync(this.request);

            return this.FetchPuttedFile();

        }

        private S3ApiModel FetchPuttedFile()
        {
            var getFileBackRequest = new GetPreSignedUrlRequest()
            {
                BucketName = this.bucketName,
                Key = this.genFileKey,
                Protocol = Protocol.HTTPS,
                Expires = DateTime.Now.AddYears(1)
            };

            return new S3ApiModel
            {
                Url = this.client.GetPreSignedURL(getFileBackRequest)
            };
        }

        public void Dispose()
        {
            this.client.Dispose();
        }
    }
}