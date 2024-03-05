package com.codingdevs.oms.repository;

import com.codingdevs.oms.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CustomerRepository extends MongoRepository<Customer, String> {}
