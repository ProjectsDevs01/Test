package com.codingdevs.oms.service;

import com.codingdevs.oms.model.Customer;
import com.codingdevs.oms.model.CustomerDTO;
import com.codingdevs.oms.model.products.Product;
import com.codingdevs.oms.repository.CustomerRepository;
import com.codingdevs.oms.repository.products.ProductRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  MongoTemplate mongoTemplate;

  public List<Customer> getAllCustomers() {
    return customerRepository.findAll();
  }

  public Customer saveCustomer(Customer customer) {
    customer.setCid(getLastCustomerCid());
    return customerRepository.save(customer);
  }

  public void deleteCustomerById(String id) {
    List<Product> productList = productRepository.findByCustomerId(id);
    if (productList.isEmpty()) {
      customerRepository.deleteById(id);
    } else {
      productRepository.deleteAll(productList);
      customerRepository.deleteById(id);
    }
  }

  public Optional<Customer> getCustomerById(String id) {
    return customerRepository.findById(id);
  }

  public Customer updateCustomer(Customer customer, String id) {
    Optional<Customer> existingOptionalCustomer = customerRepository.findById(
      id
    );
    if (existingOptionalCustomer.isPresent()) {
      Customer existingCustomer = existingOptionalCustomer.get();

      existingCustomer.setSalutation(customer.getSalutation());
      existingCustomer.setClientName(customer.getClientName());
      existingCustomer.setClientType(customer.getClientType());
      existingCustomer.setPurpose(customer.getPurpose());
      existingCustomer.setPhone(customer.getPhone());
      existingCustomer.setAddress(customer.getAddress());
      existingCustomer.setEmailAddress(customer.getEmailAddress());
      existingCustomer.setCompanyName(customer.getCompanyName());
      existingCustomer.setGstNumber(customer.getGstNumber());

      return customerRepository.save(existingCustomer);
    } else {
      throw new IllegalStateException("Client not found");
    }
  }

  public List<CustomerDTO> getNameAndId() {
    return customerRepository
      .findAll()
      .stream()
      .map(customer ->
        new CustomerDTO(
          customer.getId(),
          customer.getClientName(),
          customer.getCid()
        )
      )
      .collect(Collectors.toList());
  }

  public Long getLastCustomerCid() {
    Pageable pageable = PageRequest.of(
      0,
      1,
      Sort.by(Sort.Direction.DESC, "cid")
    );
    List<Customer> result = customerRepository.findAll(pageable).getContent();
    return result.isEmpty() ? 1 : result.get(0).getCid() + 1;
  }
}
