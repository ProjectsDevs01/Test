package com.codingdevs.oms.service.dispatch;

import com.codingdevs.oms.model.customer.Customer;
import com.codingdevs.oms.model.dispatch.Entry;
import com.codingdevs.oms.model.dispatch.EntryImage;
import com.codingdevs.oms.repository.customer.CustomerRepository;
import com.codingdevs.oms.repository.dispatch.EntryRepository;
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
public class EntryService {

  @Autowired
  private EntryRepository entryRepository;

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private GridFsTemplate gridFsTemplate;

  public Entry saveEntry(Entry entry, List<MultipartFile> imageFile)
    throws IOException {
    Optional<Customer> customer = customerRepository.findById(
      entry.getCustomerId()
    );
    if (customer.isPresent()) {
      List<EntryImage> entryImages = new ArrayList<>();
      for (MultipartFile file : imageFile) {
        byte[] compressedImage = compressImg(file.getBytes());
        EntryImage image = new EntryImage();
        image.setImageName(file.getOriginalFilename());
        ObjectId objectId = gridFsTemplate.store(
          new ByteArrayInputStream(compressedImage),
          file.getOriginalFilename(),
          file.getContentType(),
          null
        );
        image.setImageId(objectId.toString());
        entryImages.add(image);
      }
      entry.setEntryImages(entryImages);
      return entryRepository.save(entry);
    }
    return null;
  }

  public List<Entry> getAllEntries(String customerId) {
    List<Entry> entries = entryRepository.findByCustomerId(customerId);
    if (entries.isEmpty() || entries == null) return null;
    for (Entry entry : entries) {
      for (EntryImage entryImage : entry.getEntryImages()) {
        GridFSFile file = gridFsTemplate.findOne(
          new Query(
            Criteria.where("_id").is(new ObjectId(entryImage.getImageId()))
          )
        );
        if (file != null) {
          try {
            GridFsResource resource = gridFsTemplate.getResource(file);
            byte[] imageBytes = StreamUtils.copyToByteArray(
              resource.getInputStream()
            );
            entryImage.setImageData(imageBytes);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }
    return entries;
  }

  public void deleteEntry(String entryId) {
    Optional<Entry> entryOptional = entryRepository.findById(entryId);
    if (entryOptional.isPresent()) {
      Entry entry = entryOptional.get();
      if (!entry.getEntryImages().isEmpty()) {
        for (EntryImage entryImage : entry.getEntryImages()) {
          gridFsTemplate.delete(
            new Query(
              Criteria.where("_id").is(new ObjectId(entryImage.getImageId()))
            )
          );
        }
      }
      entryRepository.delete(entry);
    }
  }

  public void deleteAllEntriesByCustomer(String customerId) {
    List<Entry> entries = entryRepository.findByCustomerId(customerId);
    if (entries != null) {
      for (Entry entry : entries) {
        if (
          entry.getEntryImages().isEmpty() || entry.getEntryImages() == null
        ) continue;
        for (EntryImage entryImage : entry.getEntryImages()) {
          gridFsTemplate.delete(
            new Query(
              Criteria.where("_id").is(new ObjectId(entryImage.getImageId()))
            )
          );
        }
      }
      entryRepository.deleteAll(entries);
    }
  }

  public Entry updateEntry(
    String entryId,
    Entry entry,
    List<MultipartFile> files
  ) throws IOException {
    Optional<Entry> optionalExixtingEntry = entryRepository.findById(entryId);
    if (optionalExixtingEntry.isPresent()) {
      Entry existingEntry = optionalExixtingEntry.get();

      existingEntry.setData(entry.getData());
      List<EntryImage> updatedImages = new ArrayList<>();

      for (EntryImage existingImage : existingEntry.getEntryImages()) {
        if (existingImage == null) continue;
        boolean imageModified = false;
        for (MultipartFile file : files) {
          if (file == null) continue;

          if (file.getOriginalFilename().equals(existingImage.getImageName())) {
            gridFsTemplate.delete(
              new Query(Criteria.where("_id").is(existingImage.getImageId()))
            );
            imageModified = true;
            break;
          }
        }

        if (!imageModified) {
          updatedImages.add(existingImage);
        }
      }

      if (files != null) {
        for (MultipartFile file : files) {
          if (file == null) continue;

          byte[] compressedImage = compressImg(file.getBytes());
          ObjectId objectId = gridFsTemplate.store(
            new ByteArrayInputStream(compressedImage),
            file.getOriginalFilename(),
            file.getContentType(),
            null
          );
          EntryImage newImage = new EntryImage();
          newImage.setImageId(objectId.toString());
          newImage.setImageName(file.getOriginalFilename());
          updatedImages.add(newImage);
        }
      }
      existingEntry.setEntryImages(updatedImages);
      return entryRepository.save(existingEntry);
    } else return null;
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
