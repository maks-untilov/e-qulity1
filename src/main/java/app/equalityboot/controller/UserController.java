package app.equalityboot.controller;

import app.equalityboot.model.Role;
import app.equalityboot.model.User;
import app.equalityboot.model.UserDetails;
import app.equalityboot.service.RoleService;
import app.equalityboot.service.UserDetailService;
import app.equalityboot.service.UserService;
import app.equalityboot.service.impl.RegisterService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
    private final RegisterService registerService;
    private RoleService roleService;

    public UserController(UserDetailService userDetailService, UserService userService, RegisterService registerService, RoleService roleService) {
        this.userDetailService = userDetailService;
        this.userService = userService;
        this.registerService = registerService;
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

    @GetMapping("/add")
    public String addUser(Model model, @AuthenticationPrincipal User loggedUser) {
        List<User> allUsers = userService.getAll();
        List<User> coordinators = allUsers.stream()
                .filter(u -> u.getRole().equals(managerRole)).toList();
        model.addAttribute("coordinators", coordinators);
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("users", allUsers);
        model.addAttribute("userRole", userRole);
        model.addAttribute("managerRole", managerRole);
        model.addAttribute("adminRole", adminRole);
        model.addAttribute("bossRole", bossRole);
        return "user";
    }

    @PostMapping("/add")
    public String addUser(Model model, @AuthenticationPrincipal User loggedUser, @RequestParam String first_name,
                          @RequestParam String last_name, @RequestParam String phone, @RequestParam String email,
                          @RequestParam String group, @RequestParam String number, @RequestParam String password,
                          @RequestParam String confirm_password) {
        if (!password.equals(confirm_password)) {
            throw new RuntimeException("password are not equal");
        }
        User register = registerService.register(first_name, last_name, phone, email, password, group);
        register.setCoordinator(userService.get(Long.parseLong(number)));
        userService.save(register);
        return "redirect:/users";
    }

    @GetMapping("/byCoordinator")
    public String getByCoordinator(Model model, @AuthenticationPrincipal User user) {
        List<User> allUsersByCoordinator = userService.getByCoordinator(user);
        model.addAttribute("loggedUser", user);
        model.addAttribute("users", allUsersByCoordinator);
        model.addAttribute("userRole", userRole);
        model.addAttribute("managerRole", managerRole);
        model.addAttribute("adminRole", adminRole);
        model.addAttribute("bossRole", bossRole);
        return "user";
    }
}
