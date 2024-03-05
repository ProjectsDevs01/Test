package com.codingdevs.oms.service.products;

import java.io.ByteArrayOutputStream;
import org.springframework.stereotype.Service;

import com.pdfcrowd.Pdfcrowd;

@Service
public class PdfService {

  public byte[] converPdf(String html) throws Exception {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    Pdfcrowd.HtmlToPdfClient client = new Pdfcrowd.HtmlToPdfClient("yashhomedecor", "8509c4e02f049abf42b26ccbe8ed5b9a");
    client.convertStringToStream(html, outputStream);
    return outputStream.toByteArray();
  }
}
