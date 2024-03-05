package com.codingdevs.oms.service.products;

import com.tinify.Tinify;
import java.io.IOException;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

  public byte[] compressImage(byte[] image) throws IOException {
    Tinify.setKey("jX0KkZNK1Py5sQLjwYbrJSc6hkqPGcKQ");
    byte[] compress = Tinify.fromBuffer(image).toBuffer();

    return compress;
  }
}
