package com.codingdevs.oms.controller.products;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.codingdevs.oms.model.products.Product;
import com.codingdevs.oms.service.products.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

  @Autowired
  private ProductService productService;

  @PostMapping("/{customerId}/{category}")
  public ResponseEntity<Product> saveProduct(
  @PathVariable String customerId,
  @PathVariable String category,
  @RequestParam Map<String, String> productData,
  @RequestParam("image") MultipartFile imageFile,
  @RequestParam(value = "limg", required = false) MultipartFile lImgFile,
  @RequestParam(value = "rimg", required = false) MultipartFile rImgFile
) throws Exception {

	System.out.println(customerId);
	System.out.println(category);
	System.out.println(productData);
  
  Product product = new Product();
  List<MultipartFile> files = new ArrayList<>();

  // Add the image file to the files list
  files.add(imageFile);

  // Add left and right image files if they are not null
  if (lImgFile != null) {
    files.add(lImgFile);
  }
  if (rImgFile != null) {
    files.add(rImgFile);
  }

  product.setCategory(category);
  product.setCustomerId(customerId);
  product.setData(productData);

  Product savedProduct = productService.saveProduct(product, files);

  return ResponseEntity.ok(savedProduct);
}


  @GetMapping("/{customerId}")
  public ResponseEntity<List<Product>> getAllProducts(
    @PathVariable String customerId
  ) throws IllegalStateException, IOException {
    List<Product> products = productService.getAllProducts(customerId);

    return ResponseEntity.ok(products);
  }

  /*@PutMapping("/{customerId}/{category}/{id}")
  public ResponseEntity<Product> updateProduct(
    @PathVariable String id,
    @RequestParam("formData") Map<String, String> productData
  ) throws IOException {
    String img = productData.get("image");
    byte[] imgData = Base64.decodeBase64(img);
    byte[] compressedImage = imageService.compressImage(imgData);
    Product product = new Product(id, productData, compressedImage);
    Product saveProduct = productService.updateProduct(id, product);
    return ResponseEntity.ok(saveProduct);
  }*/

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
    productService.deleteProduct(id);
    return ResponseEntity.noContent().build();
  }
}
