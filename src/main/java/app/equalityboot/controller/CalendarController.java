package app.equalityboot.controller;

import app.equalityboot.model.OrderUser;
import app.equalityboot.model.User;
import app.equalityboot.model.UserWorkDetails;
import app.equalityboot.service.OrderService;
import app.equalityboot.service.OrderUserService;
import app.equalityboot.service.UserService;
import app.equalityboot.service.UserWorkDetailsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Controller
@RequestMapping("/calendar")
public class CalendarController {
    private UserWorkDetails.Shift notWorking = UserWorkDetails.Shift.NOT_WORKING;
    private UserWorkDetails.Shift dayWorking = UserWorkDetails.Shift.DAY;
    private UserWorkDetails.Shift nightWorking = UserWorkDetails.Shift.NIGHT;
    private UserWorkDetails.Shift dayNightWorking = UserWorkDetails.Shift.DAY_NIGHT;
    private UserService userService;
    private UserWorkDetailsService userWorkDetailsService;
    private OrderService orderService;
    private OrderUserService orderUserService;

    public CalendarController(UserService userService, UserWorkDetailsService userWorkDetailsService,
                              OrderService orderService, OrderUserService orderUserService) {
        this.userService = userService;
        this.userWorkDetailsService = userWorkDetailsService;
        this.orderService = orderService;
        this.orderUserService = orderUserService;
    }

    @GetMapping("/user")
    public String getForUser(Model model, @AuthenticationPrincipal User user) {
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
        return "calendarForUser";
    }

    @GetMapping("/user/allow/{orderUserId}")
    public String postForUser(@PathVariable String orderUserId) {
        OrderUser orderUser = orderUserService.get(Long.parseLong(orderUserId));
        orderUser.setValue(true);
        orderUserService.save(orderUser);
        return "redirect:/calendar/user";
    }

    @PostMapping
    public String post(Model model, @AuthenticationPrincipal User user, @RequestParam String shift) {
        return "redirect:/calendar";
    }

    @GetMapping("/user/next")
    public String getNext(Model model, @AuthenticationPrincipal User user) {
        LocalDateTime timeNow = LocalDateTime.now();
        // Проверяем, является ли startDate понедельником (DayOfWeek.MONDAY - 1)
        if (timeNow.getDayOfWeek().getValue() != DayOfWeek.MONDAY.getValue()) {
            // Если нет, вычитаем соответствующее количество дней для получения предыдущего понедельника
            int daysUntilPreviousMonday = timeNow.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
            timeNow = timeNow.minusDays(daysUntilPreviousMonday);
        }
        List<LocalDate> dates = timeNow.plusDays(7).toLocalDate()
                .datesUntil(timeNow.toLocalDate().plusDays(15))
                .toList();
        model.addAttribute("notWorking", notWorking);
        model.addAttribute("dayWorking", dayWorking);
        model.addAttribute("nightWorking", nightWorking);
        model.addAttribute("userWorkDetailsService", userWorkDetailsService);
        model.addAttribute("orderService", orderService);
        model.addAttribute("orderUserService", orderUserService);
        model.addAttribute("dates", dates);
        model.addAttribute("loggedUser", user);
        model.addAttribute("user", user);
        return "calendarForUser";
    }

    @GetMapping("/coordinator/next")
    public String getNextForCoordinator(Model model, @AuthenticationPrincipal User user) {
        LocalDateTime timeNow = LocalDateTime.now();
        // Проверяем, является ли startDate понедельником (DayOfWeek.MONDAY - 1)
        if (timeNow.getDayOfWeek().getValue() != DayOfWeek.MONDAY.getValue()) {
            // Если нет, вычитаем соответствующее количество дней для получения предыдущего понедельника
            int daysUntilPreviousMonday = timeNow.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
            timeNow = timeNow.minusDays(daysUntilPreviousMonday);
        }
        List<LocalDate> dates = timeNow.plusDays(7).toLocalDate()
                .datesUntil(timeNow.toLocalDate().plusDays(15))
                .toList();
        model.addAttribute("notWorking", notWorking);
        model.addAttribute("dayWorking", dayWorking);
        model.addAttribute("nightWorking", nightWorking);
        model.addAttribute("userWorkDetailsService", userWorkDetailsService);
        model.addAttribute("orderService", orderService);
        model.addAttribute("orderUserService", orderUserService);
        model.addAttribute("dates", dates);
        model.addAttribute("loggedUser", user);
        model.addAttribute("user", user);
        return "calendarForCoordinator";
    }

    @GetMapping("/user/prev")
    public String getPrevForUser(Model model, @AuthenticationPrincipal User user) {
        LocalDateTime timeNow = LocalDateTime.now();
        // Проверяем, является ли startDate понедельником (DayOfWeek.MONDAY - 1)
        if (timeNow.getDayOfWeek().getValue() != DayOfWeek.MONDAY.getValue()) {
            // Если нет, вычитаем соответствующее количество дней для получения предыдущего понедельника
            int daysUntilPreviousMonday = timeNow.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
            timeNow = timeNow.minusDays(daysUntilPreviousMonday);
        }
        List<LocalDate> dates = timeNow.minusDays(7).toLocalDate()
                .datesUntil(timeNow.toLocalDate())
                .toList();
        model.addAttribute("notWorking", notWorking);
        model.addAttribute("dayWorking", dayWorking);
        model.addAttribute("nightWorking", nightWorking);
        model.addAttribute("userWorkDetailsService", userWorkDetailsService);
        model.addAttribute("orderService", orderService);
        model.addAttribute("orderUserService", orderUserService);
        model.addAttribute("dates", dates);
        model.addAttribute("loggedUser", user);
        model.addAttribute("user", user);
        return "calendarForUser";
    }

    @GetMapping("/coordinator/prev")
    public String getPrevForCoordinator(Model model, @AuthenticationPrincipal User user) {
        LocalDateTime timeNow = LocalDateTime.now();
        // Проверяем, является ли startDate понедельником (DayOfWeek.MONDAY - 1)
        if (timeNow.getDayOfWeek().getValue() != DayOfWeek.MONDAY.getValue()) {
            // Если нет, вычитаем соответствующее количество дней для получения предыдущего понедельника
            int daysUntilPreviousMonday = timeNow.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
            timeNow = timeNow.minusDays(daysUntilPreviousMonday);
        }
        List<LocalDate> dates = timeNow.minusDays(7).toLocalDate()
                .datesUntil(timeNow.toLocalDate())
                .toList();
        model.addAttribute("notWorking", notWorking);
        model.addAttribute("dayWorking", dayWorking);
        model.addAttribute("nightWorking", nightWorking);
        model.addAttribute("userWorkDetailsService", userWorkDetailsService);
        model.addAttribute("orderService", orderService);
        model.addAttribute("orderUserService", orderUserService);
        model.addAttribute("dates", dates);
        model.addAttribute("loggedUser", user);
        model.addAttribute("user", user);
        return "calendarForCoordinator";
    }

    @GetMapping("/edit")
    public String getEditCalendar(Model model, @AuthenticationPrincipal User loggedUser) {
        LocalDateTime timeNow = LocalDateTime.now();
        // Проверяем, является ли startDate понедельником (DayOfWeek.MONDAY - 1)
        if (timeNow.getDayOfWeek().getValue() != DayOfWeek.MONDAY.getValue()) {
            // Если нет, вычитаем соответствующее количество дней для получения предыдущего понедельника
            int daysUntilPreviousMonday = timeNow.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
            timeNow = timeNow.minusDays(daysUntilPreviousMonday);
        }
        List<LocalDate> dates = timeNow.toLocalDate().datesUntil(timeNow.toLocalDate().plusDays(7)).toList();
        model.addAttribute("userWorkDetailsService", userWorkDetailsService);
        model.addAttribute("dates", dates);
        model.addAttribute("loggedUser", loggedUser);
        return "editCalendarForUser";
    }

    @PostMapping("/edit")
    public String postEditCalendar(Model model, @AuthenticationPrincipal User user, @RequestParam Map<String, String> formData) {
        LocalDateTime timeNow = LocalDateTime.now();
        // Проверяем, является ли startDate понедельником (DayOfWeek.MONDAY - 1)
        if (timeNow.getDayOfWeek().getValue() != DayOfWeek.MONDAY.getValue()) {
            // Если нет, вычитаем соответствующее количество дней для получения предыдущего понедельника
            int daysUntilPreviousMonday = timeNow.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
            timeNow = timeNow.minusDays(daysUntilPreviousMonday);
        }
        List<LocalDate> dates = timeNow.toLocalDate().datesUntil(timeNow.toLocalDate().plusDays(7)).toList();
        // Пройдитесь по каждой дате и сохраните объект "UserWorkDetails"
        for (int i = 0; i <= dates.size()-1; i++) {
            LocalDate date = dates.get(i);
            String zminaValue = formData.get("zmina_" + i);
            if (zminaValue == null) {
                continue; // Пропустить даты без выбранной смены (если пользователь не выбрал)
            }
            String stringDateFromZmina = zminaValue.substring(0, 10);
            String shiftValue = zminaValue.substring(11, 12);
            LocalDate dateFromZmina = LocalDate.parse(stringDateFromZmina);

            UserWorkDetails userWorkDetails = userWorkDetailsService.getByUserBetweenTime(user,
                    dateFromZmina.atTime(0, 0),
                    dateFromZmina.atTime(23, 59));
            if (userWorkDetails == null) {
                userWorkDetails = new UserWorkDetails();
                userWorkDetails.setUser(user);
                userWorkDetails.setStartDateTime(dateFromZmina.atTime(0, 0));
                userWorkDetails.setFinishDateTime(dateFromZmina.atTime(0, 0));
            } else {
                userWorkDetails.setStartDateTime(dateFromZmina.atTime(0, 0));
                userWorkDetails.setFinishDateTime(dateFromZmina.atTime(0, 0));
            }
            switch (shiftValue) {
                case "0":
                    userWorkDetails.setShift(notWorking);
                    userWorkDetails.setAccepted(false);
                    break;
                case "1":
                    userWorkDetails.setShift(dayWorking);
                    userWorkDetails.setAccepted(false);
                    break;
                case "2":
                    userWorkDetails.setShift(nightWorking);
                    userWorkDetails.setAccepted(false);
                    break;
                case "3":
                    userWorkDetails.setShift(dayNightWorking);
                    userWorkDetails.setAccepted(false);
                    break;
                default:
                    userWorkDetails.setShift(notWorking);
                    userWorkDetails.setAccepted(false);
            }
            userWorkDetailsService.save(userWorkDetails);
        }
        model.addAttribute("userWorkDetailsService", userWorkDetailsService);
        model.addAttribute("dates", dates);
        model.addAttribute("loggedUser", user);
        return "redirect:/calendar/user";
    }

    @GetMapping("/coordinator/{userId}")
    public String get(Model model, @AuthenticationPrincipal User user, @PathVariable String userId) {
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
        model.addAttribute("user", userService.get(Long.parseLong(userId)));
        return "calendarForCoordinator";
    }

    @GetMapping("/coordinator/edit/{userId}")
    public String getEditForCoordinator(Model model, @AuthenticationPrincipal User user, @PathVariable String userId) {
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
        model.addAttribute("user", userService.get(Long.parseLong(userId)));
        return "editCalendarByCoordinator";
    }

    @PostMapping("/coordinator/edit/{userId}")
    public String postEditForCoordinator(Model model, @AuthenticationPrincipal User loggedUser,
                                         @PathVariable String userId, @RequestParam Map<String, String> formData) {
        User user = userService.get(Long.parseLong(userId));
        LocalDateTime timeNow = LocalDateTime.now();
        // Проверяем, является ли startDate понедельником (DayOfWeek.MONDAY - 1)
        if (timeNow.getDayOfWeek().getValue() != DayOfWeek.MONDAY.getValue()) {
            // Если нет, вычитаем соответствующее количество дней для получения предыдущего понедельника
            int daysUntilPreviousMonday = timeNow.getDayOfWeek().getValue() - DayOfWeek.MONDAY.getValue();
            timeNow = timeNow.minusDays(daysUntilPreviousMonday);
        }
        List<LocalDate> dates = timeNow.toLocalDate().datesUntil(timeNow.toLocalDate().plusDays(7)).toList();
        // Пройдитесь по каждой дате и сохраните объект "UserWorkDetails"
        for (int i = 0; i <= dates.size()-1; i++) {
            LocalDate date = dates.get(i);
            String zminaValue = formData.get("zmina_" + i);
            if (zminaValue == null) {
                continue; // Пропустить даты без выбранной смены (если пользователь не выбрал)
            }
            String stringDateFromZmina = zminaValue.substring(0, 10);
            String shiftValue = zminaValue.substring(11, 12);
            LocalDate dateFromZmina = LocalDate.parse(stringDateFromZmina);

            UserWorkDetails userWorkDetails = userWorkDetailsService.getByUserBetweenTime(user,
                    dateFromZmina.atTime(0, 0),
                    dateFromZmina.atTime(23, 59));
            if (userWorkDetails == null) {
                userWorkDetails = new UserWorkDetails();
                userWorkDetails.setUser(user);
                userWorkDetails.setStartDateTime(dateFromZmina.atTime(0, 0));
                userWorkDetails.setFinishDateTime(dateFromZmina.atTime(0, 0));
            } else {
                userWorkDetails.setStartDateTime(dateFromZmina.atTime(0, 0));
                userWorkDetails.setFinishDateTime(dateFromZmina.atTime(0, 0));
            }
            switch (shiftValue) {
                case "0":
                    userWorkDetails.setShift(notWorking);
                    break;
                case "1":
                    userWorkDetails.setShift(dayWorking);
                    break;
                case "2":
                    userWorkDetails.setShift(nightWorking);
                    break;
                case "3":
                    userWorkDetails.setShift(dayNightWorking);
                    break;
                default:
                    userWorkDetails.setShift(notWorking);
            }
            userWorkDetailsService.save(userWorkDetails);
        }
        model.addAttribute("userWorkDetailsService", userWorkDetailsService);
        model.addAttribute("dates", dates);
        model.addAttribute("loggedUser", user);
        return "redirect:calendar/coordinator/" + user.getId();
    }

    @GetMapping("/coordinator")
    public String getForCoordinator(Model model, @AuthenticationPrincipal User user) {
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
        return "calendarForCoordinator";
    }

    @GetMapping("/allow/{userId}/{date}")
    public String allow(@PathVariable Long userId, @PathVariable String date) {
        User user = userService.get(userId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);
        UserWorkDetails userWorkDetails = userWorkDetailsService.getByUserBetweenTime(user, localDate.atTime(0, 0),
                localDate.atTime(23, 59));
        userWorkDetails.setAccepted(true);
        userWorkDetailsService.save(userWorkDetails);
        return "redirect:/calendar/coordinator/" + user.getId().toString();
    }
    @GetMapping("/cancel/{userId}/{date}")
    public String cancel(@PathVariable Long userId, @PathVariable String date) {
        User user = userService.get(userId);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);
        UserWorkDetails userWorkDetails = userWorkDetailsService.getByUserBetweenTime(user, localDate.atTime(0, 0),
                localDate.atTime(23, 59));
        userWorkDetails.setShift(notWorking);
        userWorkDetails.setAccepted(true);
        userWorkDetailsService.save(userWorkDetails);
        return "redirect:/calendar/coordinator/" + user.getId().toString();
    }
}

