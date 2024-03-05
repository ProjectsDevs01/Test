package com.codingdevs.oms.service.products;

import com.codingdevs.oms.model.products.Image;
import com.codingdevs.oms.model.products.Product;
import com.codingdevs.oms.repository.products.ProductRepository;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
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
        List<Image> images = new ArrayList<>();
        for (MultipartFile file : files) {
            byte[] compressedImage = compressImg(file.getBytes());

            Image image = new Image();
            image.setImageName(file.getOriginalFilename());
            image.setImageData(compressedImage);

            // Store compressed image in GridFS
            DBObject metaData = new BasicDBObject();
            metaData.put("type", "image");
            metaData.put("productId", product.getId());

            ObjectId objectId = gridFsTemplate.store(new ByteArrayInputStream(compressedImage), file.getOriginalFilename(), file.getContentType(), metaData);
            image.setImageId(objectId.toString());

            images.add(image);
        }

        product.setImages(images);

        return productRepository.save(product);
  }

  public void deleteProduct(String id) {
    productRepository.deleteById(id);
  }

  /*public Product updateProduct(String id, Product product) {
    Optional<Product> optionalProduct = productRepository.findById(id);
    if (optionalProduct.isPresent()) {
      Product existingProduct = optionalProduct.get();

      existingProduct.setImageIds(product.getImageIds());
      existingProduct.setData(product.getData());

      return productRepository.save(existingProduct);
    } else {
      throw new IllegalStateException("Product not found");
    }
  }*/

  public Optional<Product> getProductById(String id) {
    return productRepository.findById(id);
  }

  public List<Product> getAllProducts(String cid) throws IllegalStateException, IOException {
    List<Product> products = productRepository.findByCustomerId(cid);
        for (Product product : products) {
            for (Image image : product.getImages()) {
                GridFSFile file = gridFsTemplate.findOne(new Query(Criteria.where("_id").is(new ObjectId(image.getImageId()))));
                if (file != null) {
                    try {
                        GridFsResource resource = gridFsTemplate.getResource(file);
                        byte[] imageBytes = StreamUtils.copyToByteArray(resource.getInputStream());
                        image.setImageData(imageBytes);
                    } catch (IOException e) {
                        // Handle exception
                        e.printStackTrace();
                    }
                }
            }
        }
        return products;
  }

  private byte[] compressImg(byte[] imageData) throws IOException {
    BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageData));

    // Resize the image to a smaller size (e.g., 800x600)
    int targetWidth = 800;
    int targetHeight = 600;
    BufferedImage resizedImage = Scalr.resize(
      image,
      Scalr.Method.QUALITY,
      Scalr.Mode.FIT_TO_WIDTH,
      targetWidth,
      targetHeight
    );

    // Create a ByteArrayOutputStream to hold the compressed image data
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

    // Write the compressed image data to the ByteArrayOutputStream with quality setting
    ImageIO.write(resizedImage, "jpg", outputStream);

    return outputStream.toByteArray();
  }
}
