package com.codingdevs.oms.controller.products;

import com.codingdevs.oms.model.products.Product;
import com.codingdevs.oms.service.products.ProductService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    @RequestParam(value = "image", required = false) MultipartFile imageFile,
    @RequestParam(value = "fimg", required = false) MultipartFile fImgFile,
    @RequestParam(value = "limg", required = false) MultipartFile lImgFile
  ) throws Exception {
    if (productData.isEmpty()) return ResponseEntity
      .badRequest()
      .header("error", "Product data is empty")
      .build();

    Product product = new Product();
    List<MultipartFile> files = new ArrayList<>();

    if (imageFile != null) files.add(imageFile);
    if (fImgFile != null) files.add(fImgFile);
    if (lImgFile != null) files.add(lImgFile);

    product.setCategory(category);
    product.setCustomerId(customerId);
    product.setData(productData);

    Product savedProduct = productService.saveProduct(product, files);

    if (savedProduct == null) return ResponseEntity
      .badRequest()
      .header("error", "Customer not found")
      .build();

    return ResponseEntity.ok(savedProduct);
  }

  @GetMapping("/{customerId}")
  public ResponseEntity<List<Product>> getAllProducts(
    @PathVariable String customerId
  ) throws IllegalStateException, IOException {
    List<Product> products = productService.getAllProducts(customerId);
    return ResponseEntity.ok(products);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Product> updateProduct(
    @PathVariable String id,
    @RequestParam Map<String, String> productData,
    @RequestParam(value = "image", required = false) MultipartFile imageFile,
    @RequestParam(value = "fimg", required = false) MultipartFile fImgFile,
    @RequestParam(value = "limg", required = false) MultipartFile lImgFile
  ) throws IOException {
    if (productData.isEmpty()) return ResponseEntity
      .badRequest()
      .header("error", "Product data is empty")
      .build();

    Product product = new Product();
    List<MultipartFile> files = new ArrayList<>();

    if (imageFile != null) files.add(imageFile);
    if (fImgFile != null) files.add(fImgFile);
    if (lImgFile != null) files.add(lImgFile);

    product.setData(productData);
    Product savedProduct = productService.updateProduct(id, product, files);

    if (savedProduct == null) return ResponseEntity
      .badRequest()
      .header("error", "Product not found")
      .build();
    return ResponseEntity.ok(savedProduct);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
    productService.deleteProduct(id);
    return ResponseEntity.noContent().build();
  }
}
