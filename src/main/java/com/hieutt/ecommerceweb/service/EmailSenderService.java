package com.hieutt.ecommerceweb.service;

public interface EmailSenderService {
    void sendEmail(String receiver, String subject, String body);
}
