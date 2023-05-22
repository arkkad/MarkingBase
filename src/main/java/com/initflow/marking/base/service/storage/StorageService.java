package com.initflow.marking.base.service.storage;

import java.io.ByteArrayOutputStream;

public interface StorageService {
    String save(ByteArrayOutputStream out, String journalName, String username);
}
