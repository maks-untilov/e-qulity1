package app.equalityboot.service.impl;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class UniqueNumberService {
    private final Random random = new Random();

    public String getUniqueNumber() {
        int number = random.nextInt(1000, 9999);
        return Integer.toString(number);
    }
}
