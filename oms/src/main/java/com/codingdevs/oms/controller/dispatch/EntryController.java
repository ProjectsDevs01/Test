package com.codingdevs.oms.controller.dispatch;

import com.codingdevs.oms.model.dispatch.Entry;
import com.codingdevs.oms.service.dispatch.EntryService;
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
@RequestMapping("/api/dispatch")
public class EntryController {

  @Autowired
  private EntryService entryService;

  @PostMapping("/{customerId}")
  public ResponseEntity<Entry> saveEntry(
    @PathVariable String customerId,
    @RequestParam Map<String, String> entryData,
    @RequestParam(value = "image", required = false) MultipartFile imageFile
  ) throws IOException {
    if (entryData.isEmpty()) return ResponseEntity
      .badRequest()
      .header("error", "Entry data is empty")
      .build();
    Entry entry = new Entry();
    List<MultipartFile> files = new ArrayList<>();
    if (imageFile != null) files.add(imageFile);
    entry.setCustomerId(customerId);
    entry.setData(entryData);
    Entry savedEntry = entryService.saveEntry(entry, files);
    if (savedEntry == null) return ResponseEntity
      .badRequest()
      .header("error", "Customer not found")
      .build();
    return ResponseEntity.ok(savedEntry);
  }

  @GetMapping("/{customerId}")
  public ResponseEntity<List<Entry>> getEntries(
    @PathVariable String customerId
  ) {
    List<Entry> entries = entryService.getAllEntries(customerId);
    return ResponseEntity.ok(entries);
  }

  @PutMapping("/{entryId}")
  public ResponseEntity<Entry> updateEntry(
    @PathVariable String entryId,
    @RequestParam Map<String, String> entryData,
    @RequestParam(value = "image", required = false) MultipartFile imageFile
  ) throws IOException {
    if (entryData.isEmpty()) return ResponseEntity
      .badRequest()
      .header("error", "Entry data is empty")
      .build();
    Entry entry = new Entry();
    List<MultipartFile> files = new ArrayList<>();
    if (imageFile != null) files.add(imageFile);
    entry.setData(entryData);
    Entry savedEntry = entryService.updateEntry(entryId, entry, files);
    if (savedEntry == null) return ResponseEntity
      .badRequest()
      .header("error", "Entry not found")
      .build();
    return ResponseEntity.ok(savedEntry);
  }

  @DeleteMapping("/{entryId}")
  public ResponseEntity<Void> deleteEntry(@PathVariable String entryId) {
    entryService.deleteEntry(entryId);
    return ResponseEntity.noContent().build();
  }
}
