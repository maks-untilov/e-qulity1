package app.equalityboot.dao;

import app.equalityboot.model.User;
import app.equalityboot.model.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDetailDao extends JpaRepository<UserDetails, Long> {
    UserDetails getUserDetailsByUser(User user);
}
