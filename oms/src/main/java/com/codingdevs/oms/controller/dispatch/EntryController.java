package com.codingdevs.oms.controller.dispatch;

import com.codingdevs.oms.model.dispatch.Entry;
import com.codingdevs.oms.service.dispatch.EntryService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dispatch")
public class EntryController {

  @Autowired
  private EntryService entryService;

  @PostMapping("/{customerId}")
  public ResponseEntity<Entry> saveEntry(
    @PathVariable String customerId,
    @RequestBody Entry entry
  ) {
    if (entry == null) return ResponseEntity.badRequest().build();
    entry.setCustomerId(customerId);
    Entry savedEntry = entryService.saveEntry(entry);
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
    @RequestBody Entry entry
  ) {
    Entry savedEntry = entryService.updateEntry(entryId, entry);
    if (savedEntry == null) return ResponseEntity.notFound().build();
    return ResponseEntity.ok(savedEntry);
  }

  @DeleteMapping("/{entryId}")
  public ResponseEntity<Void> deleteEntry(@PathVariable String entryId) {
    entryService.deleteEntry(entryId);
    return ResponseEntity.noContent().build();
  }
}
