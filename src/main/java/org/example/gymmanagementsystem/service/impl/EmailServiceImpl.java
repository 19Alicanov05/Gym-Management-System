package org.example.gymmanagementsystem.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.gymmanagementsystem.service.EmailService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender mailSender;

    @Override
    public void sendSimpleEmail(String to, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(to);
            message.setSubject(subject);
            message.setText(text);
            message.setFrom("behlul0577@gmail.com");
            mailSender.send(message);
            log.info("Simple email sent successfully to {}", to);
        } catch (Exception e) {
            log.error("Error sending email to {}: {}", to, e.getMessage(), e);
        }
    }

    @Override
    public void sendEmail(String to, String subject, String body) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(body, true);
            mailSender.send(message);
            log.info("Email successfully sent to: {}", to);
        } catch (Exception e) {
            log.error("Email sending failed: {}", e.getMessage());
            throw new RuntimeException("Error sending email: " + e.getMessage());
        }
    }

    @Override
    public void sendEmailWithAttachment(String toEmail, String subject, String body, MultipartFile file) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("imranalicanov885@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject(subject);
            helper.setText(body, true);

            if (file != null && !file.isEmpty()) {
                String fileName = Objects.requireNonNullElse(file.getOriginalFilename(), "attachment.pdf");

                InputStreamSource source = new ByteArrayResource(file.getBytes());
                helper.addAttachment(fileName, source);
            }

            mailSender.send(message);
            log.info("Email with attachment sent successfully!");

        } catch (MessagingException | IOException e) {
            log.error("Error sending email: {}", e.getMessage());
            throw new RuntimeException("Error sending email with attachment: " + e.getMessage());
        }
    }
}
