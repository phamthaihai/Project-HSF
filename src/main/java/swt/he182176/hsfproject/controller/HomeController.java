package swt.he182176.hsfproject.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import swt.he182176.hsfproject.entity.Course;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.repository.EnrollmentRepository;
import swt.he182176.hsfproject.service.CourseAdminService;

import java.util.List;


@Controller
public class HomeController {

    private final CourseAdminService courseService;
    private final EnrollmentRepository enrollmentRepository;

    public HomeController(CourseAdminService courseService,
                          EnrollmentRepository enrollmentRepository) {
        this.courseService = courseService;
        this.enrollmentRepository = enrollmentRepository;
    }

    @GetMapping("/")
    public String home(HttpSession session, Model model) {

        List<Course> courses = courseService.getPublishedCourses();
        model.addAttribute("courses", courses);

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        if(user != null){

            List<Course> enrolledCourses =
                    enrollmentRepository.findApprovedCoursesByUserId(user.getId());

            model.addAttribute("enrolledCourses", enrolledCourses);
        }

        return "homepage";
    }
}
