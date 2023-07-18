package app.equalityboot.service.impl;

import app.equalityboot.model.Role;
import app.equalityboot.model.User;
import app.equalityboot.model.UserDetails;
import app.equalityboot.service.RoleService;
import app.equalityboot.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegisterService {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final UniqueNumberService uniqueNumberService;
    private final UserService userService;
    private final ColorService colorService;
    private RoleService roleService;

    public RegisterService(UniqueNumberService uniqueNumberService, UserService userService, ColorService colorService, RoleService roleService) {
        this.uniqueNumberService = uniqueNumberService;
        this.userService = userService;
        this.colorService = colorService;
        this.roleService = roleService;
    }

    public User register(String first_name, String last_name, String phone,
                         String email, String password) {
        User user = new User();
        user.setRole(roleService.getByRoleName("USER"));
        user.setAllowed(false);
        user.setPhoneNumber(phone);
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        user.setFirstName(first_name);
        user.setLastName(last_name);
        return userService.save(user);
    }
}
