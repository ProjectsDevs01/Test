package com.codingdevs.oms.model.dispatch;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Map;

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
  private Map<String, String> data;
  private List<EntryImage> entryImages;
}
