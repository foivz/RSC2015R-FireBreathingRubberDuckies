using PdfSharp;
using PdfSharp.Pdf;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Web;
using TheArtOfDev.HtmlRenderer.PdfSharp;

namespace src.Helpers.Api.Pdf
{
    public static class Pdf
    {
        public static void CreateDocument(string viewHtml, Stream stream)
        {
            PdfDocument pdf = PdfGenerator.GeneratePdf(viewHtml, PageSize.A4);
            pdf.Save(stream, false);
        }
    }

}