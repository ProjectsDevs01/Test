package com.codingdevs.oms.model.products;

import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "products")
@Data
@NoArgsConstructor
public class Product {

  @Id
  private String id;

  private String category;
  private String customerId;

  private Map<String, String> data;
  private List<Image> images;
}
