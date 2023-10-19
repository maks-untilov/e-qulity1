package app.equalityboot.service.impl;

import app.equalityboot.dao.UserDao;
import app.equalityboot.model.Role;
import app.equalityboot.model.User;
import app.equalityboot.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {
    private UserDao userDao;

    public UserServiceImpl(UserDao userDao) {
        this.userDao = userDao;
    }

    @Override
    public User save(User user) {
        return userDao.save(user);
    }

    @Override
    public List<User> getAll() {
        return userDao.findAll();
    }

    @Override
    public User get(Long id) {
        return userDao.getReferenceById(id);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.getUserByEmail(username);
    }

    @Override
    public User getByFirstNameAndLastName(String firstName, String lastName) {
        return userDao.getUserByFirstNameAndLastName(firstName, lastName);
    }

    @Override
    public List<User> getByCoordinator(User coordinator) {
        return userDao.getUsersByCoordinator(coordinator);
    }

    @Override
    public List<User> getUserByRole(Role role) {
        return userDao.getUsersByRole(role);
    }

    @Override
    public User getUserByToken(String token) {
        return userDao.getUsersByConfirmationToken(token);
    }
}
