package com.codingdevs.oms.service.products;

import com.codingdevs.oms.model.products.ProductImage;
import com.codingdevs.oms.model.products.Product;
import com.codingdevs.oms.repository.products.ProductRepository;
import com.mongodb.client.gridfs.model.GridFSFile;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.imageio.ImageIO;
import org.bson.types.ObjectId;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProductService {

  @Autowired
  private ProductRepository productRepository;

  @Autowired
  private GridFsTemplate gridFsTemplate;

  public Product saveProduct(Product product, List<MultipartFile> files)
    throws IOException {
    List<ProductImage> images = new ArrayList<>();
    for (int i = 0; i < files.size(); i++) {
      MultipartFile file = files.get(i);
      byte[] compressedImage = compressImg(file.getBytes());
      //byte[] compressedImage = file.getBytes();

      ProductImage image = new ProductImage();
      // image.setImageName(file.getOriginalFilename());
      if (i == 0) image.setImageName("image"); else if (
        i == 1
      ) image.setImageName("fimg"); else image.setImageName("limg");

      //image.setImageData(compressedImage);

      /*DBObject metaData = new BasicDBObject();
      metaData.put("type", "image");
      metaData.put("productId", product.getId());*/

      ObjectId objectId = gridFsTemplate.store(
        new ByteArrayInputStream(compressedImage),
        file.getOriginalFilename(),
        file.getContentType(),
        null
      );
      image.setImageId(objectId.toString());

      images.add(image);
    }

    product.setImages(images);

    return productRepository.save(product);
  }

  public void deleteProduct(String id) {
    Optional<Product> optionalProduct = productRepository.findById(id);
    if (optionalProduct.isPresent()) {
      Product product = optionalProduct.get();
      if (!product.getImages().isEmpty()) {
        for (ProductImage image : product.getImages()) {
          gridFsTemplate.delete(
            new Query(
              Criteria.where("_id").is(new ObjectId(image.getImageId()))
            )
          );
        }
      }
      productRepository.deleteById(id);
    }
  }

  public void deleteAllProductsByCustomer(String cid) {
    List<Product> products = productRepository.findByCustomerId(cid);
    if (products != null) {
      for (Product product : products) {
        for (ProductImage image : product.getImages()) {
          gridFsTemplate.delete(
            new Query(
              Criteria.where("_id").is(new ObjectId(image.getImageId()))
            )
          );
        }
      }
      productRepository.deleteAll(products);
    }
  }

  public Product updateProduct(
    String id,
    Product product,
    List<MultipartFile> files
  ) throws IOException {
    Optional<Product> optionalProduct = productRepository.findById(id);
    if (optionalProduct.isPresent()) {
      Product existingProduct = optionalProduct.get();

      for (ProductImage image : existingProduct.getImages()) {
        gridFsTemplate.delete(
          new Query(Criteria.where("_id").is(new ObjectId(image.getImageId())))
        );
      }
      existingProduct.setData(product.getData());

      List<ProductImage> images = new ArrayList<>();
      for (int i = 0; i < files.size(); i++) {
        MultipartFile file = files.get(i);
        byte[] compressedImage = compressImg(file.getBytes());
        ProductImage image = new ProductImage();
        if (i == 0) image.setImageName("image"); else if (
          i == 1
        ) image.setImageName("fimg"); else image.setImageName("limg");
        ObjectId objectId = gridFsTemplate.store(
          new ByteArrayInputStream(compressedImage),
          image.getImageName(),
          file.getContentType(),
          null
        );
        image.setImageId(objectId.toString());
        images.add(image);
      }
      existingProduct.setImages(images);

      return productRepository.save(existingProduct);
    } else {
      throw new IllegalStateException("Product not found");
    }
  }

  public Optional<Product> getProductById(String id) {
    return productRepository.findById(id);
  }

  public List<Product> getAllProducts(String cid)
    throws IllegalStateException, IOException {
    List<Product> products = productRepository.findByCustomerId(cid);
    if (products.isEmpty()) return null;
    for (Product product : products) {
      for (ProductImage image : product.getImages()) {
        GridFSFile file = gridFsTemplate.findOne(
          new Query(Criteria.where("_id").is(new ObjectId(image.getImageId())))
        );
        if (file != null) {
          try {
            GridFsResource resource = gridFsTemplate.getResource(file);
            byte[] imageBytes = StreamUtils.copyToByteArray(
              resource.getInputStream()
            );
            image.setImageData(imageBytes);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return products;
  }

  private byte[] compressImg(byte[] imageData) throws IOException {
    BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageData));

    int targetWidth = 800;
    int targetHeight = 600;
    BufferedImage resizedImage = Scalr.resize(
      image,
      Scalr.Method.QUALITY,
      Scalr.Mode.FIT_TO_WIDTH,
      targetWidth,
      targetHeight
    );

    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    ImageIO.write(resizedImage, "jpg", outputStream);

    return outputStream.toByteArray();
  }
}
