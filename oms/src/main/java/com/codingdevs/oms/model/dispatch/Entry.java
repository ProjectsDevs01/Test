package com.codingdevs.oms.model.dispatch;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "entries")
@Getter
@Setter
@ToString
public class Entry {

  @Id
  private String id;

  private String customerId;
  private String customerName;
  private String areaOfRoom;
  private String catalogName;
  private String quantity;
  private String quantityOrdered;
  private String companyName;
  private String orderNum;
  private String docNumber;
  private String transitInformation;
}
