package app.equalityboot.controller;

import app.equalityboot.model.Role;
import app.equalityboot.model.User;
import app.equalityboot.model.UserDetails;
import app.equalityboot.service.RoleService;
import app.equalityboot.service.UserDetailService;
import app.equalityboot.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {
    private final Role userRole = new Role(Role.RoleName.USER);
    private final Role adminRole = new Role(Role.RoleName.ADMIN);
    private final Role managerRole = new Role(Role.RoleName.MANAGER);
    private final Role bossRole = new Role(Role.RoleName.BOSS);
    private final UserDetailService userDetailService;
    private final UserService userService;
    private RoleService roleService;

    public UserController(UserDetailService userDetailService, UserService userService, RoleService roleService) {
        this.userDetailService = userDetailService;
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String get(Model model, @AuthenticationPrincipal User user) {
        List<User> allUsers = userService.getAll();
        List<User> coordinators = allUsers.stream()
                .filter(u -> u.getRole().equals(managerRole)).toList();
        model.addAttribute("coordinators", coordinators);
        model.addAttribute("loggedUser", user);
        model.addAttribute("users", allUsers);
        model.addAttribute("userRole", userRole);
        model.addAttribute("managerRole", managerRole);
        model.addAttribute("adminRole", adminRole);
        model.addAttribute("bossRole", bossRole);
        return "user";
    }

    @GetMapping("/{userId}")
    public String get(Model model, @PathVariable("userId") String userId, @AuthenticationPrincipal User loggedUser) {
        User user = userService.get(Long.parseLong(userId));
        model.addAttribute("user", user);
        model.addAttribute("loggedUser", loggedUser);
        return "editUser";
    }
    @PostMapping("/{userId}")
    public String activateUser(Model model, @PathVariable("userId") String userId, @AuthenticationPrincipal User loggedUser) {
        User user = userService.get(Long.parseLong(userId));
        user.setAllowed(true);
        userService.save(user);
        model.addAttribute("user", user);
        model.addAttribute("loggedUser", loggedUser);
        return "editUser";
    }

    @GetMapping("/questionnaire/{userId}")
    public String getUserDetail(Model model, @PathVariable("userId") String userId, @AuthenticationPrincipal User loggedUser) {
        User user = userService.get(Long.parseLong(userId));
        UserDetails userDetails = userDetailService.getByUser(user);
        model.addAttribute("user", user);
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("userDetail", userDetails);
        return "editQuestionnaire";
    }
}
