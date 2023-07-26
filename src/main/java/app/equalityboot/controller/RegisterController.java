package app.equalityboot.controller;

import app.equalityboot.model.Role;
import app.equalityboot.service.impl.RegisterService;
import app.equalityboot.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class RegisterController {
    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final UserService userService;
    private final RegisterService registerService;

    public RegisterController(UserService userService, RegisterService registerService) {
        this.userService = userService;
        this.registerService = registerService;
    }

    @GetMapping("/register")
    public String get(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String post(Model model, @RequestParam String first_name,
                       @RequestParam String last_name, @RequestParam String phone,
                       @RequestParam String email, @RequestParam String password,
                       @RequestParam String confirm_password) {
        if (!password.equals(confirm_password)) {
            throw new RuntimeException("password are not equal");
        }
        registerService.register(first_name, last_name, phone, email, password, "USER");
        return "redirect:/questionnaire";
    }
}
