package com.codingdevs.oms.controller.products;

import com.codingdevs.oms.service.products.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pdf")
public class PdfController {

  @Autowired
  private PdfService pdfService;

  @PostMapping("/convert")
  public ResponseEntity<Resource> convertPdf(@RequestBody String htmlString)
    throws Exception {
    byte[] pdfBytes = pdfService.converPdf(htmlString);

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_PDF);
    headers.setContentDispositionFormData("attachment", "pdf.pdf");
    ByteArrayResource resource = new ByteArrayResource(pdfBytes);

    return ResponseEntity.ok().headers(headers).body(resource);
  }
}
