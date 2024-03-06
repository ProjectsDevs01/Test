package com.codingdevs.oms.repository.customer;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.codingdevs.oms.model.customer.Customer;

public interface CustomerRepository extends MongoRepository<Customer, String> {}
