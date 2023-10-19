package app.equalityboot.controller;

import app.equalityboot.model.User;
import app.equalityboot.model.UserWorkDetails;
import app.equalityboot.service.EmailSenderService;
import app.equalityboot.service.UserService;
import app.equalityboot.service.UserWorkDetailsService;
import app.equalityboot.service.impl.EmailSenderServiceImpl;
import jakarta.mail.MessagingException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/inject")
public class InjectController {
    private final EmailSenderServiceImpl emailSenderService;
    private final UserService userService;
    private final String MY_EMAIL = "maksimuntilov7@gmail.com";

    public InjectController(EmailSenderServiceImpl emailSenderService, UserService userService) {
        this.emailSenderService = emailSenderService;
        this.userService = userService;
    }

    @GetMapping
    public String get() throws MessagingException {
        emailSenderService.sendEmail(MY_EMAIL, "Test Subject", "Test Body");
        return "injected";
    }

    @GetMapping("/allow")
    public String getAllow() {
        List<User> users = userService.getAll();
        for (User user : users) {
            user.setEmailAllowed(true);
            userService.save(user);
        }
        return "sucsses";
    }
}
