package com.codingdevs.oms.controller.invoice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.codingdevs.oms.model.invoice.Invoice;
import com.codingdevs.oms.service.invoice.InvoiceService;

@RestController
@RequestMapping("/api/invoice")
public class InvoiceController {

    @Autowired
    private InvoiceService invoiceService;

    @PostMapping("/{customerId}")
    public ResponseEntity<Invoice> saveInvoice(@PathVariable String customerId, @RequestBody Invoice invoice) {
        if(invoice == null) return ResponseEntity.badRequest().build();
        invoice.setCustomerId(customerId);
        Invoice savedInvoice = invoiceService.saveInvoice(invoice);
        return ResponseEntity.ok(savedInvoice);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<List<Invoice>> getInvoices(@PathVariable String customerId) {
        List<Invoice> invoices = invoiceService.getAllInvoices(customerId);
        return ResponseEntity.ok(invoices);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Invoice> updateInvoice(@PathVariable String id, @RequestBody Invoice invoice) {
        Invoice savedInvoice = invoiceService.updateInvoice(id, invoice);
        if(savedInvoice == null) return ResponseEntity.badRequest().build();
        return ResponseEntity.ok(savedInvoice);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable String id) {
        invoiceService.deleteInvoiceById(id);
        return ResponseEntity.noContent().build();
    }
}
