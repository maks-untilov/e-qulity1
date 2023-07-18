package app.equalityboot.controller;

import app.equalityboot.model.User;
import app.equalityboot.model.UserWorkDetails;
import app.equalityboot.service.UserWorkDetailsService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/calendar")
public class CalendarController {
    private String notWorking = UserWorkDetails.Shift.NOT_WORKING.toString();
    private String dayWorking = UserWorkDetails.Shift.DAY.toString();
    private String nightWorking = UserWorkDetails.Shift.NIGHT.toString();

    private UserWorkDetailsService userWorkDetailsService;

    public CalendarController(UserWorkDetailsService userWorkDetailsService) {
        this.userWorkDetailsService = userWorkDetailsService;
    }

    @GetMapping
    public String get(Model model, @AuthenticationPrincipal User user) {
        LocalDate now = LocalDate.now();
        List<LocalDate> dates = now.datesUntil(now.plusDays(7))
                .toList();
        model.addAttribute("userWorkDetailsService", userWorkDetailsService);
        model.addAttribute("dates", dates);
        model.addAttribute("loggedUser", user);
        return "calendar";
    }

    @PostMapping
    public String post(Model model, @AuthenticationPrincipal User user) {
        LocalDate now = LocalDate.now();
        List<LocalDate> dates = now.datesUntil(now.plusDays(7))
                .toList();
        model.addAttribute("notWorking", notWorking);
        model.addAttribute("dayWorking", dayWorking);
        model.addAttribute("nightWorking", nightWorking);
        model.addAttribute("userWorkDetailsService", userWorkDetailsService);
        model.addAttribute("dates", dates);
        model.addAttribute("loggedUser", user);
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
}

