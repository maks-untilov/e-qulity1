package app.equalityboot.controller;

import app.equalityboot.model.Role;
import app.equalityboot.model.User;
import app.equalityboot.model.UserWorkDetails;
import app.equalityboot.service.EmailSenderService;
import app.equalityboot.service.RoleService;
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
    private final RoleService roleService;
    private final String MY_EMAIL = "maksimuntilov7@gmail.com";

    public InjectController(EmailSenderServiceImpl emailSenderService,
                            UserService userService, RoleService roleService) {
        this.emailSenderService = emailSenderService;
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String get() throws MessagingException {
        emailSenderService.sendEmail(MY_EMAIL, "Test Subject", "Test Body");
        return "injected";
    }

    @GetMapping("/roles")
    public String getAllRoles() {
        Role user = new Role(Role.RoleName.USER);
        Role coordinator = new Role(Role.RoleName.MANAGER);
        Role admin = new Role(Role.RoleName.ADMIN);
        Role boss = new Role(Role.RoleName.BOSS);
        roleService.save(user);
        roleService.save(coordinator);
        roleService.save(admin);
        roleService.save(boss);
        return "injected roles";
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
