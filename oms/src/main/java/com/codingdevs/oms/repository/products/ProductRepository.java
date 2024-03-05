package com.codingdevs.oms.repository.products;

import com.codingdevs.oms.model.products.Product;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProductRepository extends MongoRepository<Product, String> {
  List<Product> findByCustomerId(String customerId);
}
