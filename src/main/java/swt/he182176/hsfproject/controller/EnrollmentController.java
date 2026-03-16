package swt.he182176.hsfproject.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import swt.he182176.hsfproject.dto.EnrollRequest;
import swt.he182176.hsfproject.entity.Course;
import swt.he182176.hsfproject.entity.Enrollment;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.repository.CourseRepository;
import swt.he182176.hsfproject.service.EnrollmentService;

@Controller
public class EnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    @Autowired
    private CourseRepository courseRepository;

    @GetMapping("/enroll/{courseId}")
    public String showEnrollForm(@PathVariable Integer courseId,
                                 HttpSession session,
                                 Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        Course course = courseRepository.findById(courseId)
                .filter(Course::isPublished)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        model.addAttribute("course", course);
        model.addAttribute("user", user);

        return "enroll";
    }

    @PostMapping("/enroll")
    public String enrollCourse(@ModelAttribute EnrollRequest request,
                               HttpSession session,
                               RedirectAttributes ra) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            Enrollment enrollment = enrollmentService.createEnrollment(request, user);

            if ("PAYOS".equalsIgnoreCase(request.getPaymentMethod())) {
                return "redirect:/payment/payos/" + enrollment.getEnrollmentId();
            }

            return "redirect:/payment/vnpay/" + enrollment.getEnrollmentId();

        } catch (RuntimeException e) {
            ra.addFlashAttribute("err", e.getMessage());
            return "redirect:/public-courses/" + request.getCourseId();
        }
    }
}