package app.equalityboot.controller;

import app.equalityboot.model.User;
import app.equalityboot.model.UserDetails;
import app.equalityboot.service.UserDetailService;
import app.equalityboot.service.UserService;
import app.equalityboot.service.ValidationService;
import app.equalityboot.service.impl.EmailSenderServiceImpl;
import jakarta.mail.MessagingException;
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
    private final ValidationService validationService;
    private final EmailSenderServiceImpl emailSenderService;

    public QuestionnaireController(UserService userService, UserDetailService userDetailService,
                                   ValidationService validationService, EmailSenderServiceImpl emailSenderService) {
        this.userService = userService;
        this.userDetailService = userDetailService;
        this.validationService = validationService;
        this.emailSenderService = emailSenderService;
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
        String errorMsg = "";
        User user = new User();
        try {
            user = userService.getByFirstNameAndLastName(first_name, last_name);
        } catch (Exception e) {
            errorMsg = "Incorrect first name or last name";
            model.addAttribute("errorMsg", errorMsg);
            return "questionnaire";
        }
        String word = validationService.validateAllLatin(first_name, last_name, number_id, sex, birth_date,
                dane_kontaktowe, education, pesel, osoba, location);
        if (word.equals("")) {
            errorMsg = "Please write the data in the columns only Latin characters";
            model.addAttribute("errorMsg", errorMsg);
            return "questionnaire";
        } else {
            userDetails.setUser(user);
            userDetails.setSex(sex);
            userDetails.setBirthDate(birth_date);
            userDetails.setContactData(dane_kontaktowe);
            userDetails.setEducation(education);
            userDetails.setPesel(pesel);
            userDetails.setOsoba(osoba);
            userDetails.setLocation(location);
            userDetailService.save(userDetails);
        }
        model.addAttribute("errorMsg", errorMsg);
        String subject = emailSenderService.getSubjectToAcceptationEmail(user.getFirstName());
        String body = emailSenderService.getBodyToAcceptationEmail(user);
        try {
            emailSenderService.sendEmail(user.getEmail(), subject, body);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        user.setCoordinator(userService.get(Long.parseLong(number_id)));
        return "redirect:/confirm" + user.getConfirmationToken();
    }
}
