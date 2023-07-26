package app.equalityboot.controller;

import app.equalityboot.model.Objects;
import app.equalityboot.model.User;
import app.equalityboot.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/profile")
public class ProfileController {

    private UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String get(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("loggedUser", user);
        model.addAttribute("user", user);
        return "profile";
    }

    @GetMapping("/{userId}")
    public String getByUser(Model model, @PathVariable String userId, @AuthenticationPrincipal User user) {
        User userByUrl = userService.get(Long.parseLong(userId));
        model.addAttribute("user", userByUrl);
        model.addAttribute("loggedUser", user);
        return "profile";
    }

    @GetMapping("/edit")
    public String getEdit() {
        return "redirect:/profile";
    }

    @PostMapping("/edit")
    public String postEdit(@AuthenticationPrincipal User user, @RequestParam(name = "phone") String phone) {
        user.setPhoneNumber(phone);
        userService.save(user);
        return "redirect:/profile";
    }
}
