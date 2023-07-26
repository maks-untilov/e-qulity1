package app.equalityboot.controller;

import app.equalityboot.model.User;
import app.equalityboot.model.UserDetails;
import app.equalityboot.service.UserDetailService;
import app.equalityboot.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/questionnaire")
public class QuestionnaireController {
    private final UserService userService;
    private final UserDetailService userDetailService;

    public QuestionnaireController(UserService userService, UserDetailService userDetailService) {
        this.userService = userService;
        this.userDetailService = userDetailService;
    }

    @GetMapping
    public String get(Model model) {
        return "questionnaire";
    }

    @PostMapping
    public String post(Model model, @RequestParam String first_name, @RequestParam String last_name,
                       @RequestParam String number_id, @RequestParam String sex,
                       @RequestParam String birth_date, @RequestParam String dane_kontaktowe,
                       @RequestParam String education, @RequestParam String pesel,
                       @RequestParam String osoba, @RequestParam String location) {
        UserDetails userDetails = new UserDetails();
        User user = userService.getByFirstNameAndLastName(first_name, last_name);
        user.setCoordinator(userService.get(Long.parseLong(number_id)));
        userDetails.setUser(user);
        userDetails.setSex(sex);
        userDetails.setBirthDate(birth_date);
        userDetails.setContactData(dane_kontaktowe);
        userDetails.setEducation(education);
        userDetails.setPesel(pesel);
        userDetails.setOsoba(osoba);
        userDetails.setLocation(location);
        userDetailService.save(userDetails);
        return "redirect:/login";
    }
}
