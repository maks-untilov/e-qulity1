package app.equalityboot.service.impl;

import app.equalityboot.dao.UserDetailDao;
import app.equalityboot.model.User;
import app.equalityboot.model.UserDetails;
import app.equalityboot.service.UserDetailService;
import app.equalityboot.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserDetailServiceImpl implements UserDetailService {
    private UserDetailDao userDetailDao;

    public UserDetailServiceImpl(UserDetailDao userDetailDao) {
        this.userDetailDao = userDetailDao;
    }

    @Override
    public UserDetails save(UserDetails userDetails) {
        return userDetailDao.save(userDetails);
    }

    @Override
    public UserDetails getById(Long id) {
        return userDetailDao.getReferenceById(id);
    }

    @Override
    public UserDetails getByUser(User user) {
        return userDetailDao.getUserDetailsByUser(user);
    }
}
