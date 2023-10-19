package app.equalityboot.controller;

import app.equalityboot.model.User;
import app.equalityboot.service.TokenService;
import app.equalityboot.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ValidationEmailController {
    private final TokenService tokenService;
    private final UserService userService;

    public ValidationEmailController(TokenService tokenService, UserService userService) {
        this.tokenService = tokenService;
        this.userService = userService;
    }

    @GetMapping("/confirm/{token}")
    public String confirmEmail(@PathVariable("token") String token, Model model) {
        User user = userService.getUserByToken(token);
            if (user != null) {
                if (tokenService.validateToken(user)) {
                    user.setEmailAllowed(true);
                    userService.save(user);
            }
        }
        return "redirect:/login";
    }

}
