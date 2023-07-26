package app.equalityboot.controller;

import app.equalityboot.model.UserWorkDetails;
import app.equalityboot.service.UserService;
import app.equalityboot.service.UserWorkDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/inject")
public class InjectController {
    private UserWorkDetailsService userWorkDetailsService;
    private UserService userService;

    public InjectController(UserWorkDetailsService userWorkDetailsService, UserService userService) {
        this.userWorkDetailsService = userWorkDetailsService;
        this.userService = userService;
    }

    @GetMapping
    public String get() {
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime finish = start.plusHours(5);
        UserWorkDetails.Shift shift = UserWorkDetails.Shift.DAY;
        UserWorkDetails userWorkDetails = new UserWorkDetails();
        userWorkDetails.setUser(userService.get(10L));
        userWorkDetails.setStartDateTime(start);
        userWorkDetails.setFinishDateTime(finish);
        userWorkDetails.setShift(shift);
        userWorkDetails.setAccepted(true);
        userWorkDetailsService.save(userWorkDetails);
        return "injected";
    }
}
