package app.equalityboot.service.impl;

import app.equalityboot.model.Role;
import app.equalityboot.model.User;
import app.equalityboot.model.UserDetails;
import app.equalityboot.service.RoleService;
import app.equalityboot.service.TokenService;
import app.equalityboot.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class RegisterService {
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    private final UserService userService;
    private final TokenService tokenService;
    private RoleService roleService;

    public RegisterService(UserService userService, TokenService tokenService, RoleService roleService) {
        this.userService = userService;
        this.tokenService = tokenService;
        this.roleService = roleService;
    }

    public User register(String first_name, String last_name, String phone,
                         String email, String password, String role) {
        User user = new User();
        user.setRole(roleService.getByRoleName(role));
        user.setAllowed(false);
        user.setPhoneNumber(phone);
        user.setEmail(email);
        user.setPassword(encoder.encode(password));
        user.setFirstName(first_name);
        user.setLastName(last_name);
        user.setConfirmationToken(tokenService.createToken());
        user.setTokenCreationDate(Instant.now());
        return userService.save(user);
    }
}
