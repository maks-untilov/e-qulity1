package app.equalityboot.controller;

import app.equalityboot.model.User;
import app.equalityboot.model.UserWorkDetails;
import app.equalityboot.service.UserService;
import app.equalityboot.service.UserWorkDetailsService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class StatController {
    private UserService userService;
    private UserWorkDetailsService userWorkDetailsService;

    public StatController(UserService userService, UserWorkDetailsService userWorkDetailsService) {
        this.userService = userService;
        this.userWorkDetailsService = userWorkDetailsService;
    }

    @GetMapping("/stat")
    public String get(Model model, @AuthenticationPrincipal User loggedUser) {
        List<User> users = userService.getAll();
        List<UserWorkDetails> userWorkDetails = userWorkDetailsService.getAll();
        LocalDateTime timeNow = LocalDateTime.now();
        // Проверяем, является ли startDate понедельником (DayOfWeek.MONDAY - 1)
        if (timeNow.getDayOfWeek().getValue() != DayOfWeek.MONDAY.getValue()) {
            // Если нет, вычитаем соответствующее количество дней для получения предыдущего понедельника
            int daysUntilPreviousMonday = timeNow.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
            timeNow = timeNow.minusDays(daysUntilPreviousMonday);
        }
        List<LocalDate> dates = timeNow.toLocalDate().datesUntil(timeNow.toLocalDate().plusDays(7)).toList();
        model.addAttribute("dates", dates);
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("users", users);
        model.addAttribute("userWorkDetailsService", userWorkDetailsService);
        model.addAttribute("workDetails", userWorkDetails);
        return "stat";
    }

    @GetMapping("/stat/{userId}")
    public String getByUser(Model model, @AuthenticationPrincipal User loggedUser, @PathVariable("userId") String userId) {
        User user = userService.get(Long.parseLong(userId));
        List<UserWorkDetails> userWorkDetails = userWorkDetailsService.getByUser(user);
        LocalDateTime timeNow = LocalDateTime.now();
        // Проверяем, является ли startDate понедельником (DayOfWeek.MONDAY - 1)
        if (timeNow.getDayOfWeek().getValue() != DayOfWeek.MONDAY.getValue()) {
            // Если нет, вычитаем соответствующее количество дней для получения предыдущего понедельника
            int daysUntilPreviousMonday = timeNow.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
            timeNow = timeNow.minusDays(daysUntilPreviousMonday);
        }
        List<LocalDate> dates = timeNow.toLocalDate().datesUntil(timeNow.toLocalDate().plusDays(7)).toList();
        model.addAttribute("user", user);
        model.addAttribute("dates", dates);
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("userWorkDetailsService", userWorkDetailsService);
        model.addAttribute("workDetails", userWorkDetails);
        return "statForUser";
    }
}
