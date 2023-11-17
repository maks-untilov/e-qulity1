package app.equalityboot.service;

import app.equalityboot.model.Objects;
import app.equalityboot.model.Order;
import app.equalityboot.model.User;
import app.equalityboot.model.UserWorkDetails;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface UserWorkDetailsService {
    UserWorkDetails save(UserWorkDetails userWorkDetails);
    UserWorkDetails getById(Long id);
    List<UserWorkDetails> getByUser(User user);
    List<UserWorkDetails> getAll();
    UserWorkDetails getByUserBetweenTime(User user, LocalDateTime start, LocalDateTime finish);
    float getTotalHoursByUserBetween(User user, LocalDate start, LocalDate finish);
    List<UserWorkDetails> getUserWorkDetailByCoordinator(User coordinator);
    List<UserWorkDetails> getAllGreaterThan(LocalDateTime start,
                                            LocalDateTime finish);
    List<UserWorkDetails> getListByUserBetweenTime(User user,
                                                   LocalDateTime start,
                                                   LocalDateTime finish);

    List<UserWorkDetails> getUserWorkDetailsByOrderAndDate(Order order, LocalDateTime start, LocalDateTime finish);

}
