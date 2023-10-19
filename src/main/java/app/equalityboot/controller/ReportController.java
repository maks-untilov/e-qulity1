package app.equalityboot.controller;

import app.equalityboot.ImageUtil;
import app.equalityboot.model.Order;
import app.equalityboot.model.User;
import app.equalityboot.model.UserWorkDetails;
import app.equalityboot.service.OrderService;
import app.equalityboot.service.OrderUserService;
import app.equalityboot.service.UserWorkDetailsService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Controller
@RequestMapping("/report")
public class ReportController {
    private OrderService orderService;
    private OrderUserService orderUserService;
    private UserWorkDetailsService userWorkDetailsService;

    public ReportController(OrderService orderService, OrderUserService orderUserService, UserWorkDetailsService userWorkDetailsService) {
        this.orderService = orderService;
        this.orderUserService = orderUserService;
        this.userWorkDetailsService = userWorkDetailsService;
    }

    @GetMapping("/add/{orderId}/{date}")
    public String get(Model model, @AuthenticationPrincipal User user, @PathVariable String orderId,
                      @PathVariable String date) {
        Order byId = orderService.getById(Long.parseLong(orderId));
        model.addAttribute("order", byId);
        model.addAttribute("loggedUser", user);
        return "report";
    }

    @PostMapping("/add/{orderId}/{date}")
    public String post(
            Model model,
            @AuthenticationPrincipal User user,
            @PathVariable String orderId,
            @PathVariable String date,
            @RequestParam("timeFrom") @DateTimeFormat(pattern = "HH:mm") LocalTime timeFrom,
            @RequestParam("timeTo") @DateTimeFormat(pattern = "HH:mm") LocalTime timeTo,
            @RequestParam("imageInput") MultipartFile file) throws IOException {
        LocalDate localDate = LocalDate.parse(date);
        UserWorkDetails userWorkDetails = userWorkDetailsService.getByUserBetweenTime(user,
                localDate.atTime(0, 0),
                localDate.atTime(23, 59));
        if (userWorkDetails == null) {
            userWorkDetails = new UserWorkDetails();
            userWorkDetails.setUser(user);
            userWorkDetails.setStartDateTime(timeFrom.atDate(localDate));
            userWorkDetails.setFinishDateTime(timeTo.atDate(localDate));
        } else {
            userWorkDetails.setStartDateTime(timeFrom.atDate(localDate));
            userWorkDetails.setFinishDateTime(timeTo.atDate(localDate));
        }
        if (!file.isEmpty()) {
            BufferedImage compressedImage = ImageUtil.compressImage(file.getBytes(), 0.5);
            byte[] compressedImageData = ImageUtil.imageToByteArray(compressedImage);
            userWorkDetails.setImage(compressedImageData);
        }
        userWorkDetailsService.save(userWorkDetails);
        Order byId = orderService.getById(Long.parseLong(orderId));
        model.addAttribute("order", byId);
        model.addAttribute("loggedUser", user);
        return "redirect:/calendar/user";
    }
}
