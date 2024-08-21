package com.springboot_backend.server.Storage;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public interface StorageInterface {

    void addDocument(String uid, String collection_id, String doc_id, Map<String, Object> data);

    void removeDocument(String uid, String collection_id, String doc_id, Map<String, Object> data);

    List<Map<String, Object>> getCollection(String uid, String collection_id)
            throws InterruptedException, ExecutionException;

    void clearUser(String uid) throws InterruptedException, ExecutionException;

    List<Map<String, Object>> getCookies(String uid, String collection_id)
            throws InterruptedException, ExecutionException, IllegalArgumentException;
}
