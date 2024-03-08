package com.codingdevs.oms.model.customer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDTO {

  private String id;
  private Long cid;
  private String clientName;

  public CustomerDTO(String id, String clientName, Long cid) {
    this.id = id;
    this.clientName = clientName;
    this.cid = cid;
  }
}
