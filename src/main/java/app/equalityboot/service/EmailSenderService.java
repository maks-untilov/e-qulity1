package app.equalityboot.service;

import jakarta.mail.MessagingException;

public interface EmailSenderService {
    void sendEmail(String to, String subject, String body) throws MessagingException;
}
