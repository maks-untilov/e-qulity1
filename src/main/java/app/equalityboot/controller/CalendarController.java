package app.equalityboot.controller;

import app.equalityboot.model.User;
import app.equalityboot.model.UserWorkDetails;
import app.equalityboot.service.UserService;
import app.equalityboot.service.UserWorkDetailsService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/calendar")
public class CalendarController {
    private UserWorkDetails.Shift notWorking = UserWorkDetails.Shift.NOT_WORKING;
    private UserWorkDetails.Shift dayWorking = UserWorkDetails.Shift.DAY;
    private UserWorkDetails.Shift nightWorking = UserWorkDetails.Shift.NIGHT;
    private UserService userService;
    private UserWorkDetailsService userWorkDetailsService;

    public CalendarController(UserService userService, UserWorkDetailsService userWorkDetailsService) {
        this.userService = userService;
        this.userWorkDetailsService = userWorkDetailsService;
    }

    @GetMapping
    public String get(Model model, @AuthenticationPrincipal User user) {
        LocalDate now = LocalDate.now();
        List<LocalDate> dates = now.datesUntil(now.plusDays(7))
                .toList();
        model.addAttribute("notWorking", notWorking);
        model.addAttribute("dayWorking", dayWorking);
        model.addAttribute("nightWorking", nightWorking);
        model.addAttribute("userWorkDetailsService", userWorkDetailsService);
        model.addAttribute("dates", dates);
        model.addAttribute("loggedUser", user);
        model.addAttribute("user", user);
        return "calendar";
    }

    @PostMapping
    public String post(Model model, @AuthenticationPrincipal User user, @RequestParam String shift) {
        return "redirect:/calendar";
    }

    @GetMapping("/{userId}")
    public String get(Model model, @AuthenticationPrincipal User user, @PathVariable String userId) {
        LocalDate now = LocalDate.now();
        User userByUrl = userService.get(Long.parseLong(userId));
        List<LocalDate> dates = now.datesUntil(now.plusDays(7))
                .toList();
        model.addAttribute("userWorkDetailsService", userWorkDetailsService);
        model.addAttribute("dates", dates);
        model.addAttribute("loggedUser", user);
        model.addAttribute("user", userByUrl);
        return "calendar";
    }

    @GetMapping("/next")
    public String getNext(Model model, @AuthenticationPrincipal User user) {
        LocalDate now = LocalDate.now();
        List<LocalDate> dates = now.plusDays(7).datesUntil(now.plusDays(15))
                .toList();
        model.addAttribute("userWorkDetailsService", userWorkDetailsService);
        model.addAttribute("dates", dates);
        model.addAttribute("loggedUser", user);
        return "calendar";
    }

    @GetMapping("/prev")
    public String getPrev(Model model, @AuthenticationPrincipal User user) {
        LocalDate now = LocalDate.now();
        List<LocalDate> dates = now.minusDays(7).datesUntil(now)
                .toList();
        model.addAttribute("userWorkDetailsService", userWorkDetailsService);
        model.addAttribute("dates", dates);
        model.addAttribute("loggedUser", user);
        return "calendar";
    }

    @GetMapping("/edit")
    public String getEditCalendar(Model model, @AuthenticationPrincipal User loggedUser) {
        LocalDate now = LocalDate.now();
        List<LocalDate> dates = now.datesUntil(now.plusDays(7))
                .toList();
        model.addAttribute("userWorkDetailsService", userWorkDetailsService);
        model.addAttribute("dates", dates);
        model.addAttribute("loggedUser", loggedUser);
        return "editCalendar";
    }

    @PostMapping("/edit")
    public String postEditCalendar(Model model, @AuthenticationPrincipal User loggedUser, @RequestParam Map<String, String> formData) {
        LocalDate now = LocalDate.now();
        List<LocalDate> dates = now.datesUntil(now.plusDays(7)).toList();

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

            UserWorkDetails userWorkDetails = new UserWorkDetails();
            userWorkDetails.setUser(loggedUser);
            userWorkDetails.setStartDateTime(dateFromZmina.atTime(0, 0));
            userWorkDetails.setFinishDateTime(dateFromZmina.atTime(0, 0));
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
                default:
                    userWorkDetails.setShift(notWorking);
            }
            userWorkDetailsService.save(userWorkDetails);
        }
        model.addAttribute("userWorkDetailsService", userWorkDetailsService);
        model.addAttribute("dates", dates);
        model.addAttribute("loggedUser", loggedUser);
        return "redirect:/calendar";
    }
}

