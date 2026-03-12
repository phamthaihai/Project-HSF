package swt.he182176.hsfproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import swt.he182176.hsfproject.entity.Enrollment;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.repository.UserRepository;
import swt.he182176.hsfproject.service.EnrollmentService;

import java.security.Principal;
import java.util.List;

@Controller
public class MyEnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/my-enrollments")
    public String myEnrollments(Model model, Principal principal) {

        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getName();

        User user = userRepository.findByEmail(email).orElseThrow();

        List<Enrollment> enrollments =
                enrollmentService.getMyEnrollments(user.getId());

        model.addAttribute("enrollments", enrollments);

        return "my-enrollments";
    }
}