package app.equalityboot.controller;

import app.equalityboot.model.Order;
import app.equalityboot.model.Role;
import app.equalityboot.model.User;
import app.equalityboot.service.OrderService;
import app.equalityboot.service.OrderUserService;
import app.equalityboot.service.RoleService;
import app.equalityboot.service.UserService;
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
    private UserService userService;
    private RoleService roleService;
    private String PURPLE_COLOR = "purple";
    private String GRAY_COLOR = "gray";
    private String DARK_GREEN_COLOR = "dark_green";
    private String LIGHT_GREEN_COLOR = "light_green";
    private String YELLOW_COLOR = "yellow";
    private String ORANGE_COLOR = "orange";
    private String BEIGE_COLOR = "beige";
    private Long COORDINATOR_ID = 2L;

    public IndexController(OrderService orderService, OrderUserService orderUserService,
                           UserService userService, RoleService roleService) {
        this.orderService = orderService;
        this.orderUserService = orderUserService;
        this.userService = userService;
        this.roleService = roleService;
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
        Role coordinator = roleService.getById(COORDINATOR_ID);
        List<User> coordinators = userService.getUserByRole(coordinator);
        List<LocalDate> dates = timeNow.toLocalDate().datesUntil(timeNow.toLocalDate().plusDays(7)).toList();
        List<Order> allWeekOrders = orderService.getOrderByDateBetween(dates.get(0), dates.get(6));
        List<Order> ordersForFirstDay = allWeekOrders.stream().filter(order -> order.getDate().equals(dates.get(0))).toList();
        List<Order> ordersForSecondDay = allWeekOrders.stream().filter(order -> order.getDate().equals(dates.get(1))).toList();
        List<Order> ordersForThirdDay = allWeekOrders.stream().filter(order -> order.getDate().equals(dates.get(2))).toList();
        List<Order> ordersForFourDay = allWeekOrders.stream().filter(order -> order.getDate().equals(dates.get(3))).toList();
        List<Order> ordersForFiveDay = allWeekOrders.stream().filter(order -> order.getDate().equals(dates.get(4))).toList();
        List<Order> ordersForSixDay = allWeekOrders.stream().filter(order -> order.getDate().equals(dates.get(5))).toList();
        List<Order> ordersForSevenDay = allWeekOrders.stream().filter(order -> order.getDate().equals(dates.get(6))).toList();
        model.addAttribute("ordersForFirstDay", ordersForFirstDay);
        model.addAttribute("ordersForSecondDay", ordersForSecondDay);
        model.addAttribute("ordersForThirdDay", ordersForThirdDay);
        model.addAttribute("ordersForFourDay", ordersForFourDay);
        model.addAttribute("ordersForFiveDay", ordersForFiveDay);
        model.addAttribute("ordersForSixDay", ordersForSixDay);
        model.addAttribute("ordersForSevenDay", ordersForSevenDay);
        model.addAttribute("coordinators", coordinators);
        model.addAttribute("purple", PURPLE_COLOR);
        model.addAttribute("gray", GRAY_COLOR);
        model.addAttribute("dark_green", DARK_GREEN_COLOR);
        model.addAttribute("light_green", LIGHT_GREEN_COLOR);
        model.addAttribute("yellow", YELLOW_COLOR);
        model.addAttribute("orange", ORANGE_COLOR);
        model.addAttribute("beige", BEIGE_COLOR);
        model.addAttribute("user", user);
        model.addAttribute("dates", dates);
        model.addAttribute("orderUserService", orderUserService);
        model.addAttribute("orderService", orderService);
        return "index";
    }

    @GetMapping("/desc")
    public String getByDesc(Model model, @AuthenticationPrincipal User user) {
        LocalDateTime timeNow = LocalDateTime.now();
        // Проверяем, является ли startDate понедельником (DayOfWeek.MONDAY - 1)
        if (timeNow.getDayOfWeek().getValue() != DayOfWeek.MONDAY.getValue()) {
            // Если нет, вычитаем соответствующее количество дней для получения предыдущего понедельника
            int daysUntilPreviousMonday = timeNow.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
            timeNow = timeNow.minusDays(daysUntilPreviousMonday);
        }
        List<LocalDate> dates = timeNow.toLocalDate().datesUntil(timeNow.toLocalDate().plusDays(7)).toList();
        List<Order> allWeekOrders = orderService.getOrderByObjectName().stream()
                .sorted(Comparator.comparing(Order::getDescription))
                .toList();
        Role coordinator = roleService.getById(COORDINATOR_ID);
        List<User> coordinators = userService.getUserByRole(coordinator);
        List<Order> ordersForFirstDay = allWeekOrders.stream().filter(order -> order.getDate().equals(dates.get(0))).toList();
        List<Order> ordersForSecondDay = allWeekOrders.stream().filter(order -> order.getDate().equals(dates.get(1))).toList();
        List<Order> ordersForThirdDay = allWeekOrders.stream().filter(order -> order.getDate().equals(dates.get(2))).toList();
        List<Order> ordersForFourDay = allWeekOrders.stream().filter(order -> order.getDate().equals(dates.get(3))).toList();
        List<Order> ordersForFiveDay = allWeekOrders.stream().filter(order -> order.getDate().equals(dates.get(4))).toList();
        List<Order> ordersForSixDay = allWeekOrders.stream().filter(order -> order.getDate().equals(dates.get(5))).toList();
        List<Order> ordersForSevenDay = allWeekOrders.stream().filter(order -> order.getDate().equals(dates.get(6))).toList();
        model.addAttribute("ordersForFirstDay", ordersForFirstDay);
        model.addAttribute("ordersForSecondDay", ordersForSecondDay);
        model.addAttribute("ordersForThirdDay", ordersForThirdDay);
        model.addAttribute("ordersForFourDay", ordersForFourDay);
        model.addAttribute("ordersForFiveDay", ordersForFiveDay);
        model.addAttribute("ordersForSixDay", ordersForSixDay);
        model.addAttribute("ordersForSevenDay", ordersForSevenDay);
        model.addAttribute("coordinators", coordinators);
        model.addAttribute("purple", PURPLE_COLOR);
        model.addAttribute("gray", GRAY_COLOR);
        model.addAttribute("dark_green", DARK_GREEN_COLOR);
        model.addAttribute("light_green", LIGHT_GREEN_COLOR);
        model.addAttribute("yellow", YELLOW_COLOR);
        model.addAttribute("orange", ORANGE_COLOR);
        model.addAttribute("beige", BEIGE_COLOR);
        model.addAttribute("user", user);
        model.addAttribute("dates", dates);
        model.addAttribute("orderUserService", orderUserService);
        model.addAttribute("orderService", orderService);
        return "index";
    }

    @GetMapping("/object")
    public String getByObjectName(Model model, @AuthenticationPrincipal User user) {
        LocalDateTime timeNow = LocalDateTime.now();
        // Проверяем, является ли startDate понедельником (DayOfWeek.MONDAY - 1)
        if (timeNow.getDayOfWeek().getValue() != DayOfWeek.MONDAY.getValue()) {
            // Если нет, вычитаем соответствующее количество дней для получения предыдущего понедельника
            int daysUntilPreviousMonday = timeNow.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
            timeNow = timeNow.minusDays(daysUntilPreviousMonday);
        }
        List<LocalDate> dates = timeNow.toLocalDate().datesUntil(timeNow.toLocalDate().plusDays(7)).toList();
        List<Order> allWeekOrders = orderService.getOrderByObjectName().stream()
                .sorted(Comparator.comparing(o -> o.getObject().getName()))
                .toList();
        Role coordinator = roleService.getById(COORDINATOR_ID);
        List<User> coordinators = userService.getUserByRole(coordinator);
        List<Order> ordersForFirstDay = allWeekOrders.stream().filter(order -> order.getDate().equals(dates.get(0))).toList();
        List<Order> ordersForSecondDay = allWeekOrders.stream().filter(order -> order.getDate().equals(dates.get(1))).toList();
        List<Order> ordersForThirdDay = allWeekOrders.stream().filter(order -> order.getDate().equals(dates.get(2))).toList();
        List<Order> ordersForFourDay = allWeekOrders.stream().filter(order -> order.getDate().equals(dates.get(3))).toList();
        List<Order> ordersForFiveDay = allWeekOrders.stream().filter(order -> order.getDate().equals(dates.get(4))).toList();
        List<Order> ordersForSixDay = allWeekOrders.stream().filter(order -> order.getDate().equals(dates.get(5))).toList();
        List<Order> ordersForSevenDay = allWeekOrders.stream().filter(order -> order.getDate().equals(dates.get(6))).toList();
        model.addAttribute("ordersForFirstDay", ordersForFirstDay);
        model.addAttribute("ordersForSecondDay", ordersForSecondDay);
        model.addAttribute("ordersForThirdDay", ordersForThirdDay);
        model.addAttribute("ordersForFourDay", ordersForFourDay);
        model.addAttribute("ordersForFiveDay", ordersForFiveDay);
        model.addAttribute("ordersForSixDay", ordersForSixDay);
        model.addAttribute("ordersForSevenDay", ordersForSevenDay);
        model.addAttribute("coordinators", coordinators);
        model.addAttribute("purple", PURPLE_COLOR);
        model.addAttribute("gray", GRAY_COLOR);
        model.addAttribute("dark_green", DARK_GREEN_COLOR);
        model.addAttribute("light_green", LIGHT_GREEN_COLOR);
        model.addAttribute("yellow", YELLOW_COLOR);
        model.addAttribute("orange", ORANGE_COLOR);
        model.addAttribute("beige", BEIGE_COLOR);
        model.addAttribute("user", user);
        model.addAttribute("dates", dates);
        model.addAttribute("orderUserService", orderUserService);
        model.addAttribute("orderService", orderService);
        return "index";
    }

    @GetMapping("/time")
    public String getByStartDate(Model model, @AuthenticationPrincipal User user) {
        LocalDateTime timeNow = LocalDateTime.now();
        // Проверяем, является ли startDate понедельником (DayOfWeek.MONDAY - 1)
        if (timeNow.getDayOfWeek().getValue() != DayOfWeek.MONDAY.getValue()) {
            // Если нет, вычитаем соответствующее количество дней для получения предыдущего понедельника
            int daysUntilPreviousMonday = timeNow.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
            timeNow = timeNow.minusDays(daysUntilPreviousMonday);
        }
        List<LocalDate> dates = timeNow.toLocalDate().datesUntil(timeNow.toLocalDate().plusDays(7)).toList();
        List<Order> allWeekOrders = orderService.getAll().stream()
                .sorted(Comparator.comparing(Order::getStartTime))
                .toList();
        Role coordinator = roleService.getById(COORDINATOR_ID);
        List<User> coordinators = userService.getUserByRole(coordinator);
        List<Order> ordersForFirstDay = allWeekOrders.stream().filter(order -> order.getDate().equals(dates.get(0))).toList();
        List<Order> ordersForSecondDay = allWeekOrders.stream().filter(order -> order.getDate().equals(dates.get(1))).toList();
        List<Order> ordersForThirdDay = allWeekOrders.stream().filter(order -> order.getDate().equals(dates.get(2))).toList();
        List<Order> ordersForFourDay = allWeekOrders.stream().filter(order -> order.getDate().equals(dates.get(3))).toList();
        List<Order> ordersForFiveDay = allWeekOrders.stream().filter(order -> order.getDate().equals(dates.get(4))).toList();
        List<Order> ordersForSixDay = allWeekOrders.stream().filter(order -> order.getDate().equals(dates.get(5))).toList();
        List<Order> ordersForSevenDay = allWeekOrders.stream().filter(order -> order.getDate().equals(dates.get(6))).toList();
        model.addAttribute("ordersForFirstDay", ordersForFirstDay);
        model.addAttribute("ordersForSecondDay", ordersForSecondDay);
        model.addAttribute("ordersForThirdDay", ordersForThirdDay);
        model.addAttribute("ordersForFourDay", ordersForFourDay);
        model.addAttribute("ordersForFiveDay", ordersForFiveDay);
        model.addAttribute("ordersForSixDay", ordersForSixDay);
        model.addAttribute("ordersForSevenDay", ordersForSevenDay);
        model.addAttribute("coordinators", coordinators);
        model.addAttribute("purple", PURPLE_COLOR);
        model.addAttribute("gray", GRAY_COLOR);
        model.addAttribute("dark_green", DARK_GREEN_COLOR);
        model.addAttribute("light_green", LIGHT_GREEN_COLOR);
        model.addAttribute("yellow", YELLOW_COLOR);
        model.addAttribute("orange", ORANGE_COLOR);
        model.addAttribute("beige", BEIGE_COLOR);
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
        Role coordinator = roleService.getById(COORDINATOR_ID);
        List<User> coordinators = userService.getUserByRole(coordinator);
        List<LocalDate> dates = startDate.datesUntil(startDate.plusDays(7)).toList();
        List<Order> allWeekOrders = orderService.getByDateGreaterThan(startDate.atTime(0, 0));
        model.addAttribute("orders", allWeekOrders);
        model.addAttribute("orderUserService", orderUserService);
        model.addAttribute("coordinators", coordinators);
        model.addAttribute("purple", PURPLE_COLOR);
        model.addAttribute("gray", GRAY_COLOR);
        model.addAttribute("dark_green", DARK_GREEN_COLOR);
        model.addAttribute("light_green", LIGHT_GREEN_COLOR);
        model.addAttribute("yellow", YELLOW_COLOR);
        model.addAttribute("orange", ORANGE_COLOR);
        model.addAttribute("beige", BEIGE_COLOR);
        model.addAttribute("user", user);
        model.addAttribute("dates", dates);
        model.addAttribute("orderService", orderService);
        return "index";
    }
}
