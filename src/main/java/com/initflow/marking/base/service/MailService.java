package com.initflow.marking.base.service;

import java.io.ByteArrayOutputStream;
import java.util.List;

public interface MailService {
    void send(ByteArrayOutputStream out, List<String> mails, String company, String wh, String pg, String journalName);

    void sendError(List<String> mails, Exception e, String company, String wh, String pg);
}
