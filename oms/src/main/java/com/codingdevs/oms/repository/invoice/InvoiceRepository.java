package com.codingdevs.oms.repository.invoice;

import com.codingdevs.oms.model.invoice.Invoice;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface InvoiceRepository extends MongoRepository<Invoice, String> {

    List<Invoice> findByCustomerId(String customerId);
}
