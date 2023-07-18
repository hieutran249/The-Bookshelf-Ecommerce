package com.hieutt.ecommerceweb.service.impl;

import com.hieutt.ecommerceweb.service.EmailSenderService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {
    private final JavaMailSender mailSender;

    public EmailSenderServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendEmail(String receiver, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("admin@tbs.com");
        message.setTo(receiver);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);
        System.out.println("mail sent");
    }
}
