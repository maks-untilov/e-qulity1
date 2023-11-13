package app.equalityboot.controller;

import app.equalityboot.ImageUtil;
import app.equalityboot.model.Order;
import app.equalityboot.model.OrderUser;
import app.equalityboot.model.User;
import app.equalityboot.model.UserWorkDetails;
import app.equalityboot.service.OrderService;
import app.equalityboot.service.OrderUserService;
import app.equalityboot.service.UserService;
import app.equalityboot.service.UserWorkDetailsService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Controller
public class GraphicsController {
    private int FIRST_DAY_OF_WEEK = 0;
    private int LAST_DAY_OF_WEEK = 6;
    private UserWorkDetailsService userWorkDetailsService;
    private UserService userService;
    private OrderService orderService;
    private OrderUserService orderUserService;

    public GraphicsController(UserWorkDetailsService userWorkDetailsService, UserService userService,
                              OrderService orderService, OrderUserService orderUserService) {
        this.userWorkDetailsService = userWorkDetailsService;
        this.userService = userService;
        this.orderService = orderService;
        this.orderUserService = orderUserService;
    }

    @GetMapping("/graphics")
    public String get(Model model, @AuthenticationPrincipal User loggedUser) {
        LocalDateTime timeNow = LocalDateTime.now();
        // Проверяем, является ли startDate понедельником (DayOfWeek.MONDAY - 1)
        if (timeNow.getDayOfWeek().getValue() != DayOfWeek.MONDAY.getValue()) {
            // Если нет, вычитаем соответствующее количество дней для получения предыдущего понедельника
            int daysUntilPreviousMonday = timeNow.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
            timeNow = timeNow.minusDays(daysUntilPreviousMonday);
        }
        List<LocalDate> dates = timeNow.toLocalDate().datesUntil(timeNow.toLocalDate().plusDays(7)).toList();
        List<Order> orderByDateBetween =
                orderService.getOrderByDateBetween(dates.get(FIRST_DAY_OF_WEEK), dates.get(LAST_DAY_OF_WEEK));
        List<OrderUser> orderUserList = new ArrayList<>();
        for (Order order : orderByDateBetween) {
            List<OrderUser> byOrder = orderUserService.getByOrder(order);
            orderUserList.addAll(byOrder);
        }
        model.getAttribute("");
        model.addAttribute("userWorkDetailService", userWorkDetailsService);
        model.addAttribute("workers", orderUserList);
        model.addAttribute("dates", dates);
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("orderService", orderService);
        return "graphics";
    }

    @GetMapping("/boss/graphics")
    public String getForBoss(Model model, @AuthenticationPrincipal User loggedUser) {
        LocalDateTime timeNow = LocalDateTime.now();
        // Проверяем, является ли startDate понедельником (DayOfWeek.MONDAY - 1)
        if (timeNow.getDayOfWeek().getValue() != DayOfWeek.MONDAY.getValue()) {
            // Если нет, вычитаем соответствующее количество дней для получения предыдущего понедельника
            int daysUntilPreviousMonday = timeNow.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
            timeNow = timeNow.minusDays(daysUntilPreviousMonday);
        }
        List<LocalDate> dates = timeNow.toLocalDate().datesUntil(timeNow.toLocalDate().plusDays(7)).toList();
        List<Order> orderByDateBetween =
                orderService.getOrderByDateBetween(dates.get(FIRST_DAY_OF_WEEK), dates.get(LAST_DAY_OF_WEEK));
        List<OrderUser> orderUserList = new ArrayList<>();
        for (Order order : orderByDateBetween) {
            List<OrderUser> byOrder = orderUserService.getByOrder(order);
            orderUserList.addAll(byOrder);
        }
        model.getAttribute("");
        model.addAttribute("userWorkDetailService", userWorkDetailsService);
        model.addAttribute("workers", orderUserList);
        model.addAttribute("dates", dates);
        model.addAttribute("imgUtil", new ImageUtil());
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("orderService", orderService);
        return "graphicsForBoss";
    }
    @PostMapping()
    public String post(@RequestParam("time-from") String timeFrom,
                       @RequestParam("time-to") String timeTo,
                       @RequestParam("paidTrue") boolean isPaid) {
        return "redirect:/graphics";
    }

    @GetMapping("/graphics/edit/{userWorkDetailsId}/{orderId}")
    public String getEdit(Model model, @AuthenticationPrincipal User loggedUser, @PathVariable() String userWorkDetailsId,
                          @PathVariable String orderId) {
        UserWorkDetails userWorkDetails = userWorkDetailsService.getById(Long.parseLong(userWorkDetailsId));
        model.addAttribute("order", orderService.getById(Long.parseLong(orderId)));
        model.addAttribute("worker", userWorkDetails);
        model.addAttribute("loggedUser", loggedUser);
        model.addAttribute("imgUtil", new ImageUtil());
        return "editGraphics";
    }
    @PostMapping("/graphics/edit/{userWorkDetailsId}/{orderId}")
    public String postEdit(Model model, @AuthenticationPrincipal User loggedUser,
                           @PathVariable() String userWorkDetailsId,
                           @RequestParam("time-from") String timeFrom,
                           @RequestParam("time-to") String timeTo,
                           @RequestParam("paidTrue") boolean isPaid,
                           @PathVariable String orderId) {
        UserWorkDetails userWorkDetails = userWorkDetailsService.getById(Long.parseLong(userWorkDetailsId));
        Order order = orderService.getById(Long.parseLong(orderId));
        LocalTime time_from = LocalTime.parse(timeFrom);
        LocalTime time_to = LocalTime.parse(timeTo);
        if (userWorkDetails == null) {
            userWorkDetails = new UserWorkDetails();
            userWorkDetails.setUser(userWorkDetails.getUser());
            userWorkDetails.setStartDateTime(time_from.atDate(order.getDate()));
            userWorkDetails.setFinishDateTime(time_to.atDate(order.getDate()));
            userWorkDetails.setPaid(isPaid);
        } else {
            userWorkDetails.setStartDateTime(time_from.atDate(order.getDate()));
            userWorkDetails.setFinishDateTime(time_to.atDate(order.getDate()));
            userWorkDetails.setPaid(isPaid);
        }
        userWorkDetailsService.save(userWorkDetails);
        model.addAttribute("worker", userWorkDetails);
        model.addAttribute("loggedUser", loggedUser);
        return "redirect:/graphics";
    }
}
