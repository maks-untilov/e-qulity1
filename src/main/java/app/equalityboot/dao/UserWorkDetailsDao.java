package app.equalityboot.dao;

import app.equalityboot.model.User;
import app.equalityboot.model.UserWorkDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Repository
public interface UserWorkDetailsDao extends JpaRepository<UserWorkDetails, Long> {
    List<UserWorkDetails> getUserWorkDetailsByUser(User user);
    @Query(value = "from UserWorkDetails u where u.user = :user AND u.startDateTime BETWEEN :startDate AND :endDate")
    UserWorkDetails getUserWorkDetailsByUserBetweenTime(@Param("user")User user,
                                                        @Param("startDate") LocalDateTime start,
                                                        @Param("endDate")LocalDateTime finish);
}
