package app.equalityboot.controller;

import app.equalityboot.model.Order;
import app.equalityboot.model.User;
import app.equalityboot.model.UserWorkDetails;
import app.equalityboot.service.OrderService;
import app.equalityboot.service.OrderUserService;
import app.equalityboot.service.UserService;
import app.equalityboot.service.UserWorkDetailsService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/panel")
public class IndexForUserController {
    private UserWorkDetails.Shift notWorking = UserWorkDetails.Shift.NOT_WORKING;
    private UserWorkDetails.Shift dayWorking = UserWorkDetails.Shift.DAY;
    private UserWorkDetails.Shift nightWorking = UserWorkDetails.Shift.NIGHT;
    private UserWorkDetailsService userWorkDetailsService;
    private OrderService orderService;
    private OrderUserService orderUserService;

    public IndexForUserController(UserWorkDetailsService userWorkDetailsService, OrderService orderService, OrderUserService orderUserService) {
        this.userWorkDetailsService = userWorkDetailsService;
        this.orderService = orderService;
        this.orderUserService = orderUserService;
    }

    @GetMapping("/worker")
    public String getWorker(Model model, @AuthenticationPrincipal User user) {
        LocalDateTime timeNow = LocalDateTime.now();
        // Проверяем, является ли startDate понедельником (DayOfWeek.MONDAY - 1)
        if (timeNow.getDayOfWeek().getValue() != DayOfWeek.MONDAY.getValue()) {
            // Если нет, вычитаем соответствующее количество дней для получения предыдущего понедельника
            int daysUntilPreviousMonday = timeNow.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
            timeNow = timeNow.minusDays(daysUntilPreviousMonday);
        }
        List<LocalDate> dates = timeNow.toLocalDate().datesUntil(timeNow.toLocalDate().plusDays(7)).toList();
        model.addAttribute("notWorking", notWorking);
        model.addAttribute("dayWorking", dayWorking);
        model.addAttribute("nightWorking", nightWorking);
        model.addAttribute("userWorkDetailsService", userWorkDetailsService);
        model.addAttribute("orderService", orderService);
        model.addAttribute("orderUserService", orderUserService);
        model.addAttribute("dates", dates);
        model.addAttribute("loggedUser", user);
        model.addAttribute("user", user);
        return "indexForUser";
    }

    @GetMapping("/coordinator")
    public String getCoordinator(Model model, @AuthenticationPrincipal User user) {
        LocalDateTime timeNow = LocalDateTime.now();
        // Проверяем, является ли startDate понедельником (DayOfWeek.MONDAY - 1)
        if (timeNow.getDayOfWeek().getValue() != DayOfWeek.MONDAY.getValue()) {
            // Если нет, вычитаем соответствующее количество дней для получения предыдущего понедельника
            int daysUntilPreviousMonday = timeNow.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
            timeNow = timeNow.minusDays(daysUntilPreviousMonday);
        }
        List<LocalDate> dates = timeNow.toLocalDate().datesUntil(timeNow.toLocalDate().plusDays(7)).toList();
        List<Order> allWeekOrders = orderService.getByDateGreaterThan(timeNow.toLocalDate().atTime(0, 0));
        model.addAttribute("orders", allWeekOrders);
        model.addAttribute("user", user);
        model.addAttribute("dates", dates);
        model.addAttribute("orderUserService", orderUserService);
        model.addAttribute("orderService", orderService);
        return "indexForCoordinator";
    }
}
