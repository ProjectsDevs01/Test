package com.codingdevs.oms.repository.customer;

import com.codingdevs.oms.model.customer.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {}
