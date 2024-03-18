package com.codingdevs.oms.model.products;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductImage {

  private String imageName;
  private String imageId;
  private byte[] imageData;
}
