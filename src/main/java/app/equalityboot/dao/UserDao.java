package app.equalityboot.dao;

import app.equalityboot.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao extends JpaRepository<User, Long> {
    User getUserByEmail(String email);
    User getUserByFirstNameAndLastName(String firstName, String lastName);
}
