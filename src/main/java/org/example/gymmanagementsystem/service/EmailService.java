package org.example.gymmanagementsystem.service;

import org.springframework.web.multipart.MultipartFile;

public interface EmailService {
    void sendEmail(String to, String subject, String body);
    void sendEmailWithAttachment(String toEmail, String subject, String body, MultipartFile file);
    void sendSimpleEmail(String to, String subject, String text);
}
