package app.equalityboot.service.impl;

import app.equalityboot.dao.UserWorkDetailsDao;
import app.equalityboot.model.Order;
import app.equalityboot.model.User;
import app.equalityboot.model.UserWorkDetails;
import app.equalityboot.service.UserWorkDetailsService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserWorkDetailsServiceImpl implements UserWorkDetailsService {
    private UserWorkDetailsDao userWorkDetailsDao;

    public UserWorkDetailsServiceImpl(UserWorkDetailsDao userWorkDetailsDao) {
        this.userWorkDetailsDao = userWorkDetailsDao;
    }

    @Override
    public UserWorkDetails save(UserWorkDetails userWorkDetails) {
        return userWorkDetailsDao.save(userWorkDetails);
    }

    @Override
    public UserWorkDetails getById(Long id) {
        return userWorkDetailsDao.getReferenceById(id);
    }

    @Override
    public List<UserWorkDetails> getByUser(User user) {
        return userWorkDetailsDao.getUserWorkDetailsByUser(user);
    }

    @Override
    public List<UserWorkDetails> getAll() {
        return userWorkDetailsDao.findAll();
    }

    @Override
    public UserWorkDetails getByUserBetweenTime(User user, LocalDateTime start, LocalDateTime finish) {
        return userWorkDetailsDao.getUserWorkDetailsByUserBetweenTime(user, start, finish);
    }

    @Override
    public float getTotalHoursByUserBetween(User user, LocalDate start, LocalDate finish) {
        List<UserWorkDetails> userWorkDetailsList = getListByUserBetweenTime(user,
                start.atTime(0, 0),
                finish.atTime(23, 59));
        Duration totalWorkDuration = Duration.ZERO;
        for (UserWorkDetails workDetails : userWorkDetailsList) {
            LocalDateTime sessionStart = workDetails.getStartDateTime();
            LocalDateTime sessionEnd = workDetails.getFinishDateTime();

            // Проверяем, перекрывается ли сеанс работы с выбранным временным диапазоном
            if (sessionEnd.isAfter(start.atTime(0, 0)) && sessionStart.isBefore(finish.atTime(23 ,59))) {
                totalWorkDuration = totalWorkDuration.plus(Duration.between(sessionStart, sessionEnd));
            }
        }

        return (float) totalWorkDuration.toMinutes() / 60;
    }

    @Override
    public List<UserWorkDetails> getUserWorkDetailByCoordinator(User coordinator) {
        return userWorkDetailsDao.findAll().stream()
                .filter(userWorkDetails -> userWorkDetails.getUser().getCoordinator() != null)
                .filter(userWorkDetails -> userWorkDetails.getUser().getCoordinator().equals(coordinator))
                .toList();
    }

    @Override
    public List<UserWorkDetails> getAllGreaterThan(LocalDateTime start, LocalDateTime finish) {
        return userWorkDetailsDao.getAllGreaterThan(start, finish);
    }

    @Override
    public List<UserWorkDetails> getListByUserBetweenTime(User user, LocalDateTime start, LocalDateTime finish) {
        return userWorkDetailsDao.getListByUserBetweenTime(user, start, finish);
    }

    @Override
    public List<UserWorkDetails> getUserWorkDetailsByOrderAndDate(Order order, LocalDateTime start, LocalDateTime finish) {
        return userWorkDetailsDao.getUserWorkDetailsByOrderAnd(order, start, finish);
    }
}
