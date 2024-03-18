package com.codingdevs.oms.model.dispatch;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EntryImage {

  private String imageName;
  private String imageId;
  private byte[] imageData;
}