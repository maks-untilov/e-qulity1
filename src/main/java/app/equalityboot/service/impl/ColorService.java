package app.equalityboot.service.impl;

import org.springframework.stereotype.Service;

import java.awt.*;
import java.util.Random;

@Service
public class ColorService {
    private final Random random = new Random();
    public Color getRandomColor() {
        Random random = new Random();
        int red;
        int green;
        int blue;

        do {
            red = random.nextInt(256);
            green = random.nextInt(256);
            blue = random.nextInt(256);
        } while (isRedOrCloseToRed(red, green, blue));

        return new Color(red, green, blue);
    }

    private boolean isRedOrCloseToRed(int red, int green, int blue) {
        // Задайте диапазон красных цветов, которые нужно исключить
        int redThreshold = 50;
        int greenThreshold = 50;
        int blueThreshold = 50;

        // Проверка, находится ли цвет в заданном диапазоне от красного цвета
        return (red >= 255 - redThreshold && green <= greenThreshold && blue <= blueThreshold);
    }
}
