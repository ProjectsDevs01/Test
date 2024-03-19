package com.codingdevs.oms.model.invoice;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("invoices")
@Getter
@Setter
@ToString
public class Invoice {

  @Id
  private String id;

  private Map<String, String> data;
  private String customerId;
  private String category;
}
