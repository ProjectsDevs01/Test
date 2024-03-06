package com.codingdevs.oms.model.products;

import java.util.List;
import java.util.Map;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Document(collection = "products")
@Getter
@Setter
@ToString
public class Product {

  @Id
  private String id;

  private String category;
  private String customerId;

  private Map<String, String> data;
  private List<Image> images;
}
