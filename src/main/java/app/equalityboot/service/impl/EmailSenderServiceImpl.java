package app.equalityboot.service.impl;


import app.equalityboot.model.User;
import app.equalityboot.service.EmailSenderService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailSenderServiceImpl implements EmailSenderService {
    private final JavaMailSender javaMailSender;

    public EmailSenderServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendEmail(String to, String subject, String body) throws MessagingException {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        javaMailSender.send(message);
    }

    public String getSubjectToAcceptationEmail(String username) {
        String text = "Cześć, " + username +"\n" +
                "Dziękuję za rejestrację.\n";
        return text;
    }

    public String getBodyToAcceptationEmail(User user) {
        String text =
                "Zakończ rejestrację, potwierdź swój e-mail.\n" +
                        "Potwierdź Email " + "https://e-quality-work.eu/confirm/" + user.getConfirmationToken();
        return text;
    }
}
