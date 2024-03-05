package com.codingdevs.oms.model.products;

import lombok.Data;

@Data
public class Image {

    private String imageName;
    private String imageId;
    private byte[] imageData;

    // Getters and setters
}
