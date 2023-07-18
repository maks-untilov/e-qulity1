package app.equalityboot.controller;

import app.equalityboot.model.Order;
import app.equalityboot.model.User;
import app.equalityboot.service.OrderService;
import app.equalityboot.service.OrderUserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Controller
@RequestMapping("/index")
public class IndexController {
    private OrderService orderService;
    private OrderUserService orderUserService;

    public IndexController(OrderService orderService, OrderUserService orderUserService) {
        this.orderService = orderService;
        this.orderUserService = orderUserService;
    }

    @GetMapping
    public String get(Model model, @AuthenticationPrincipal User user) {
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
        return "index";
    }

    @GetMapping("/{startDate}")
    public String getWeek(Model model, @AuthenticationPrincipal User user, @PathVariable("startDate") String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd", Locale.US);
        LocalDate startDate = LocalDate.parse(date, formatter);
        // Проверяем, является ли startDate понедельником (DayOfWeek.MONDAY - 1)
        if (startDate.getDayOfWeek().getValue() != DayOfWeek.MONDAY.getValue()) {
            // Если нет, вычитаем соответствующее количество дней для получения предыдущего понедельника
            int daysUntilPreviousMonday = startDate.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
            startDate = startDate.minusDays(daysUntilPreviousMonday);
        }
        List<LocalDate> dates = startDate.datesUntil(startDate.plusDays(7)).toList();
        List<Order> allWeekOrders = orderService.getByDateGreaterThan(startDate.atTime(0, 0));
        model.addAttribute("orders", allWeekOrders);
        model.addAttribute("orderUserService", orderUserService);
        model.addAttribute("user", user);
        model.addAttribute("dates", dates);
        model.addAttribute("orderService", orderService);
        return "index";
    }
}
