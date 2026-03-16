package swt.he182176.hsfproject.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import swt.he182176.hsfproject.entity.Enrollment;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.service.EnrollmentService;

import java.util.List;

@Controller
public class MyEnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @GetMapping("/my-enrollments")
    public String myEnrollments(Model model, HttpSession session) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        List<Enrollment> enrollments = enrollmentService.getMyEnrollments(user.getId());
        model.addAttribute("enrollments", enrollments);

        return "my-enrollments";
    }
}