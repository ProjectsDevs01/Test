package com.codingdevs.oms.service.invoice;

import com.codingdevs.oms.model.customer.Customer;
import com.codingdevs.oms.model.invoice.Invoice;
import com.codingdevs.oms.repository.customer.CustomerRepository;
import com.codingdevs.oms.repository.invoice.InvoiceRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InvoiceService {

  @Autowired
  private InvoiceRepository invoiceRepository;

  @Autowired
  private CustomerRepository customerRepository;

  public Invoice saveInvoice(Invoice invoice, String customerId) {
    Optional<Customer> customer = customerRepository.findById(customerId);
    if (customer.isPresent()) {
      invoice.setCustomerId(customerId);
      return invoiceRepository.save(invoice);
    }
    return null;
  }

  public List<Invoice> getAllInvoices(String customerId) {
    List<Invoice> invoices = invoiceRepository.findByCustomerId(customerId);
    if (invoices.isEmpty() || invoices == null) return null;
    return invoices;
  }

  public void deleteAllInvoicesByCustomer(String customerId) {
    List<Invoice> invoices = invoiceRepository.findByCustomerId(customerId);
    if (invoices != null) {
      invoiceRepository.deleteAll(invoices);
    }
  }

  public void deleteInvoiceById(String id) {
    invoiceRepository.deleteById(id);
  }

  public Invoice updateInvoice(String id, Invoice invoice) {
    Optional<Invoice> existingOptionalInvoice = invoiceRepository.findById(id);
    if (existingOptionalInvoice.isPresent()) {
      Invoice existingInvoice = existingOptionalInvoice.get();
      existingInvoice.setArea(invoice.getArea());
      existingInvoice.setGstAmount(invoice.getGstAmount());
      existingInvoice.setGstPercentage(invoice.getGstPercentage());
      existingInvoice.setRate(invoice.getRate());
      existingInvoice.setQuantity(invoice.getQuantity());
      existingInvoice.setTotal(invoice.getTotal());
      existingInvoice.setAmount(invoice.getAmount());

      return invoiceRepository.save(existingInvoice);
    } else {
      throw new IllegalStateException("Invoice not found");
    }
  }
}
