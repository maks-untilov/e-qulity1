package app.equalityboot.service.impl;

import app.equalityboot.dao.UserWorkDetailsDao;
import app.equalityboot.model.User;
import app.equalityboot.model.UserWorkDetails;
import app.equalityboot.service.UserWorkDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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
        return userWorkDetailsDao.getUserWorkDetailsByUserAndStartDateTimeBetween(user, start, finish);
    }

    @Override
    public UserWorkDetails getTotalHoursByUserBetween(User user, LocalDate start, LocalDate finish) {
        return null;
    }
}
