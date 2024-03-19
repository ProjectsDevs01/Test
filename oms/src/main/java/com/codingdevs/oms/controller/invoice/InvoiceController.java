package com.codingdevs.oms.controller.invoice;

import com.codingdevs.oms.model.invoice.Invoice;
import com.codingdevs.oms.service.invoice.InvoiceService;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {

  @Autowired
  private InvoiceService invoiceService;

  @PostMapping("/{customerId}/{category}")
  public ResponseEntity<?> saveInvoice(
    @PathVariable String customerId,
    @PathVariable String category,
    @RequestParam Map<String, String> invoiceData
  ) {
    if (invoiceData == null) return ResponseEntity.badRequest().build();
    Invoice savedInvoice = invoiceService.saveInvoice(customerId, category, invoiceData);
    if (savedInvoice == null) return ResponseEntity.ok("Customer not found");
    return ResponseEntity.ok(savedInvoice);
  }

  @GetMapping("/{customerId}")
  public ResponseEntity<List<Invoice>> getInvoices(
    @PathVariable String customerId
  ) {
    List<Invoice> invoices = invoiceService.getAllInvoices(customerId);
    return ResponseEntity.ok(invoices);
  }

  @PutMapping("/{id}")
  public ResponseEntity<?> updateInvoice(
    @PathVariable String id,
    @RequestParam Map<String, String> invoiceData
  ) {
    Invoice savedInvoice = invoiceService.updateInvoice(id, invoiceData);
    if (savedInvoice == null) return ResponseEntity.ok("Customer not found");
    return ResponseEntity.ok(savedInvoice);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteInvoice(@PathVariable String id) {
    invoiceService.deleteInvoiceById(id);
    return ResponseEntity.noContent().build();
  }
}
