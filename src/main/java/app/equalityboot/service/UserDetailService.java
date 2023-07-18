package app.equalityboot.service;

import app.equalityboot.model.User;
import app.equalityboot.model.UserDetails;

public interface UserDetailService {
    UserDetails save(UserDetails userDetails);
    UserDetails getById(Long id);
    UserDetails getByUser(User user);
}
