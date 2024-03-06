package com.codingdevs.oms.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "customers")
@Data
public class Customer {

  @Id
  private String id;

  private Long cid;
  private String salutation;
  private String clientName;
  private String clientType;
  private String purpose;
  private String address;
  private String phone;
  private String emailAddress;
  private String companyName;
  private String gstNumber;
}
