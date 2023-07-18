package app.equalityboot.dao;

import app.equalityboot.model.User;
import app.equalityboot.model.UserWorkDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserWorkDetailsDao extends JpaRepository<UserWorkDetails, Long> {
    List<UserWorkDetails> getUserWorkDetailsByUser(User user);
    UserWorkDetails getUserWorkDetailsByUserAndStartDateTimeBetween(User user, LocalDateTime start, LocalDateTime finish);
}
