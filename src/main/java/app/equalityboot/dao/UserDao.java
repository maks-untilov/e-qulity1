package app.equalityboot.dao;

import app.equalityboot.model.Role;
import app.equalityboot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
    User getUserByEmail(String email);
    User getUserByFirstNameAndLastName(String firstName, String lastName);
    List<User> getUsersByCoordinator(User coordinator);
    List<User> getUsersByRole(Role role);
    User getUsersByConfirmationToken(String token);
}
