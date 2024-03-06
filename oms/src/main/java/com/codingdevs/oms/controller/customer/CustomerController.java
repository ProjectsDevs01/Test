package com.codingdevs.oms.controller.customer;

import com.codingdevs.oms.model.customer.Customer;
import com.codingdevs.oms.model.customer.CustomerDTO;
import com.codingdevs.oms.service.customer.CustomerService;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

  @Autowired
  private CustomerService customerService;

  @PostMapping
  public ResponseEntity<Customer> saveCustomer(@RequestBody Customer customer) {
    Customer saveCustomer = customerService.saveCustomer(customer);

    return ResponseEntity.ok(saveCustomer);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Customer> getCustomer(@PathVariable String id) {
    Optional<Customer> customer = customerService.getCustomerById(id);
    return customer
      .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
      .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
  }

  @GetMapping
  public ResponseEntity<List<Customer>> getAllCustomer() {
    List<Customer> customers = customerService.getAllCustomers();

    return ResponseEntity.ok(customers);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Customer> updateCustomer(
    @PathVariable String id,
    @RequestBody Customer customer
  ) {
    Customer saveCustomer = customerService.updateCustomer(customer, id);
    return ResponseEntity.ok(saveCustomer);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCustomer(@PathVariable String id) {
    customerService.deleteCustomerById(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/names")
  public ResponseEntity<List<CustomerDTO>> getNameAndId() {
    List<CustomerDTO> customerDTO = customerService.getNameAndId();

    return ResponseEntity.ok(customerDTO);
  }
}
