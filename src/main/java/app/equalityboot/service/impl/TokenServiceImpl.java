package app.equalityboot.service.impl;

import app.equalityboot.model.User;
import app.equalityboot.service.TokenService;
import app.equalityboot.service.UserService;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {
    private final UserService userService;

    public TokenServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public String createToken() {
        return UUID.randomUUID().toString();
    }

    @Override
    public boolean validateToken(User user) {
        if (user != null) {
            Instant tokenCreationDate = user.getTokenCreationDate();
            Instant now = Instant.now();
            if (Duration.between(tokenCreationDate, now).toHours() <= 24) {
                user.setEmailAllowed(true);
                userService.save(user);
                return true;
            }
        }
        return false;
    }
}
