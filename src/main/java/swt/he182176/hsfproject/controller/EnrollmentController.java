package swt.he182176.hsfproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import swt.he182176.hsfproject.dto.EnrollRequest;
import swt.he182176.hsfproject.entity.Enrollment;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.repository.UserRepository;
import swt.he182176.hsfproject.service.EnrollmentService;

import java.security.Principal;

@Controller
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/enroll")
    public String enrollCourse(@ModelAttribute EnrollRequest request,
                               Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Enrollment enrollment =
                enrollmentService.createEnrollment(request, user);

        if ("PAYOS".equals(request.getPaymentMethod())) {
            return "redirect:/payment/payos/" + enrollment.getEnrollmentId();
        }

        return "redirect:/payment/vnpay/" + enrollment.getEnrollmentId();
    }
}