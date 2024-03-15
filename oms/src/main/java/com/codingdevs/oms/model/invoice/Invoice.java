package com.codingdevs.oms.model.invoice;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("invoices")
@Getter
@Setter
@ToString
public class Invoice {

  @Id
  private String id;

  private String customerId;
  private String customerName;
  private String area;
  private String quantity;
  private String rate;
  private String gstPercentage;
  private String amount;
  private String gstAmount;
  private String total;
}
