package app.equalityboot.service;

import app.equalityboot.model.User;

public interface TokenService {
    String createToken();
    boolean validateToken(User user);
}
