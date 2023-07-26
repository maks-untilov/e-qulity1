package app.equalityboot.service;

import app.equalityboot.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {
    User save(User user);
    List<User> getAll();
    User get(Long id);
    User getByFirstNameAndLastName(String firstName, String lastName);
    List<User> getByCoordinator(User user);
}
