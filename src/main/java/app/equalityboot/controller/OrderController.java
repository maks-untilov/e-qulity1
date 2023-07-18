package app.equalityboot.controller;

import app.equalityboot.model.*;
import app.equalityboot.service.ObjectsService;
import app.equalityboot.service.OrderUserService;
import app.equalityboot.service.UserService;
import app.equalityboot.service.impl.OrderReservationService;
import app.equalityboot.service.OrderService;
import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/orders")
public class OrderController {
    private OrderService orderService;
    private ObjectsService objectsService;
    private OrderReservationService reservationService;
    private UserService userService;
    private OrderUserService orderUserService;

    public OrderController(OrderService orderService, ObjectsService objectsService, 
                           OrderReservationService reservationService, 
                           UserService userService, OrderUserService orderUserService) {
        this.orderService = orderService;
        this.objectsService = objectsService;
        this.reservationService = reservationService;
        this.userService = userService;
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
        List<Order> allWeekOrders = orderService.getByDateGreaterThan(timeNow.minusWeeks(1));
        model.addAttribute("orderUserService", orderUserService);
        model.addAttribute("location", allWeekOrders);
        model.addAttribute("user", user);
        model.addAttribute("dates", dates);
        return "orders";
    }

    @PostMapping
    public String post(Model model, @AuthenticationPrincipal User user, @RequestParam String startTime,
                       @RequestParam String finishTime, @RequestParam Integer workers,
                       @RequestParam String objectName, @RequestParam String description) {
        LocalDateTime startDate = LocalDateTime.parse(startTime,
                DateTimeFormatter.ofPattern("MM d yyyy HH':'mm", Locale.GERMANY));
        LocalDateTime finishDate = LocalDateTime.parse(finishTime,
                DateTimeFormatter.ofPattern("MM d yyyy HH':'mm", Locale.GERMANY));
        Order order = reservationService.reserve(objectName, startDate, finishDate, workers, List.of(), description);
        LocalDateTime timeNow = LocalDateTime.now();
        List<Order> allWeekOrders = orderService.getByDateGreaterThan(timeNow.minusWeeks(1));
        model.addAttribute("orderUserService", orderUserService);
        model.addAttribute("location", allWeekOrders);
        model.addAttribute("user", user);
        return "orders";
    }

    @GetMapping("/edit/{orderId}")
    public String get(Model model, @PathVariable("orderId") String orderId, @AuthenticationPrincipal User user) {
        Order order = orderService.getById(Long.parseLong(orderId));
        model.addAttribute("order", order);
        model.addAttribute("loggedUser", user);
        model.addAttribute("orderUserService", orderUserService);
        return "editOrder";
    }

    @PostMapping("/edit/{orderId}")
    public String post(Model model, @PathVariable("orderId") String orderId,
                       @RequestParam String description, @AuthenticationPrincipal User user) {
        Order order = orderService.getById(Long.parseLong(orderId));
        order.setDescription(description);
        orderService.save(order);
        model.addAttribute("order", order);
        model.addAttribute("loggedUser", user);
        model.addAttribute("orderUserService", orderUserService);
        return "orders";
    }
    @GetMapping("/edit/workers/{orderId}")
    public String getWorkers(Model model, @PathVariable("orderId") String orderId, @AuthenticationPrincipal User user) {
        List<User> workers = userService.getAll().stream()
                .filter(u -> u.getRole().getRoleName().equals(Role.RoleName.USER) && u.isAllowed())
                .toList();
        Order order = orderService.getById(Long.parseLong(orderId));
        model.addAttribute("workers", workers);
        model.addAttribute("order", order);
        model.addAttribute("loggedUser", user);
        model.addAttribute("orderUserService", orderUserService);
        return "orderWorkers";
    }

    @PostMapping("/edit/workers/{orderId}/workerId")
    public String postWorkers(Model model, @PathVariable("orderId") String orderId,
                        @AuthenticationPrincipal User user) {
        List<User> workers = userService.getAll().stream()
                .filter(u -> u.getRole().getRoleName().equals(Role.RoleName.USER))
                .toList();
        Order order = orderService.getById(Long.parseLong(orderId));
        model.addAttribute("workers", workers);
        model.addAttribute("order", order);
        model.addAttribute("orderUserService", orderUserService);
        model.addAttribute("loggedUser", user);
        return "orderWorkers";
    }

    @GetMapping("/add/{orderId}/{userId}")
    public String getAddWorkers(Model model, @PathVariable("orderId") String orderId,
                                @PathVariable("userId") String userId, @AuthenticationPrincipal User user) {
        List<User> workers = userService.getAll().stream()
                .filter(u -> u.getRole().getRoleName().equals(Role.RoleName.USER) && u.isAllowed())
                .toList();
        User worker = userService.get(Long.parseLong(userId));
        Order order = orderService.getById(Long.parseLong(orderId));

        // Создание нового UserOrder с другим пользователем
        OrderUser newOrderUser = new OrderUser();
        newOrderUser.setOrder(order);
        newOrderUser.setUser(worker);
        newOrderUser.setValue(false);
        orderUserService.save(newOrderUser);

        model.addAttribute("workers", workers);
        model.addAttribute("order", order);
        model.addAttribute("loggedUser", user);
        model.addAttribute("orderUserService", orderUserService);
        return "orderWorkers";
    }
}
