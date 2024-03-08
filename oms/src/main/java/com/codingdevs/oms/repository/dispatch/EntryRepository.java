package com.codingdevs.oms.repository.dispatch;

import com.codingdevs.oms.model.dispatch.Entry;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface EntryRepository extends MongoRepository<Entry, String> {
    public List<Entry> findByCustomerId(String customerId);
}
