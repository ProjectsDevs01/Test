package com.codingdevs.oms.service.dispatch;

import com.codingdevs.oms.model.customer.Customer;
import com.codingdevs.oms.model.dispatch.Entry;
import com.codingdevs.oms.repository.customer.CustomerRepository;
import com.codingdevs.oms.repository.dispatch.EntryRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EntryService {

  @Autowired
  private EntryRepository entryRepository;

  @Autowired
  private CustomerRepository customerRepository;

  public Entry saveEntry(Entry entry, String customerId) {
    Optional<Customer> customer = customerRepository.findById(customerId);
    if (customer.isPresent()) {
      entry.setCustomerId(customerId);
      return entryRepository.save(entry);
    }
    return null;
  }

  public List<Entry> getAllEntries(String customerId) {
    List<Entry> entries = entryRepository.findByCustomerId(customerId);
    if (entries.isEmpty() || entries == null) return null;
    return entries;
  }

  public void deleteEntry(String entryId) {
    entryRepository.deleteById(entryId);
  }

  public void deleteAllEntriesByCustomer(String customerId) {
    List<Entry> entries = entryRepository.findByCustomerId(customerId);
    if (entries != null) {
      entryRepository.deleteAll(entries);
    }
  }

  public Entry updateEntry(String entryId, Entry entry) {
    Optional<Entry> optionalExixtingEntry = entryRepository.findById(entryId);
    if (optionalExixtingEntry.isPresent()) {
      Entry existingEntry = optionalExixtingEntry.get();
      existingEntry.setAreaOfRoom(entry.getAreaOfRoom());
      existingEntry.setCatalogName(entry.getCatalogName());
      existingEntry.setQuantity(entry.getQuantity());
      existingEntry.setQuantityOrdered(entry.getQuantityOrdered());
      existingEntry.setCompanyName(entry.getCompanyName());
      existingEntry.setOrderNum(entry.getOrderNum());
      existingEntry.setDocNumber(entry.getDocNumber());
      existingEntry.setTransitInformation(entry.getTransitInformation());

      return entryRepository.save(existingEntry);
    } else {
      throw new IllegalStateException("Entry not found");
    }
  }
}
