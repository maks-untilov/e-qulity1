package app.equalityboot.controller;

import app.equalityboot.model.User;
import app.equalityboot.model.UserDetails;
import app.equalityboot.service.UserDetailService;
import app.equalityboot.service.UserService;
import app.equalityboot.service.impl.PDFGeneratorService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/document/download")
public class PDFDocumentController {
    private final PDFGeneratorService pdfGeneratorService;
    private final UserDetailService userDetailService;
    private final UserService userService;

    public PDFDocumentController(PDFGeneratorService pdfGeneratorService, UserDetailService userDetailService,
                                 UserService userService) {
        this.pdfGeneratorService = pdfGeneratorService;
        this.userDetailService = userDetailService;
        this.userService = userService;
    }

    @GetMapping("/user-document/{userId}")
    public void generateUserDocument(HttpServletResponse response, @PathVariable String userId) {
        User user = userService.get(Long.parseLong(userId));
        UserDetails userDetails = userDetailService.getByUser(user);
        response.setContentType("application/pdf");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=pdf_"
                + userDetails.getUser().getFirstName() + "_"
                + userDetails.getUser().getLastName() + ".pdf";
        response.setHeader(headerKey, headerValue);
        this.pdfGeneratorService.export(response, userDetails);
    }
}
