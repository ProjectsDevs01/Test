package com.codingdevs.oms.model.customer;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "customers")
@Getter
@Setter
@ToString
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
