package swt.he182176.hsfproject.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import swt.he182176.hsfproject.entity.Course;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.repository.EnrollmentRepository;
import swt.he182176.hsfproject.service.CourseAdminService;
import swt.he182176.hsfproject.service.PostService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class HomeController {

    private final CourseAdminService courseService;
    private final EnrollmentRepository enrollmentRepository;
    private final PostService postService;

    public HomeController(CourseAdminService courseService,
                          EnrollmentRepository enrollmentRepository,
                          PostService postService) {
        this.courseService = courseService;
        this.enrollmentRepository = enrollmentRepository;
        this.postService = postService;
    }

    @GetMapping("/")
    public String home(HttpSession session, Model model) {

        List<Course> courses = courseService.getPublishedCourses();
        if (courses.size() > 4) {
            courses = courses.subList(0, 4);
        }
        model.addAttribute("courses", courses);

        model.addAttribute("latestPosts", postService.getLatestPosts());

        User user = (User) session.getAttribute("user");
        model.addAttribute("user", user);

        if (user != null) {
            List<Course> enrolledCourses =
                    enrollmentRepository.findApprovedCoursesByUserId(user.getId());

            Set<Integer> enrolledCourseIds = new HashSet<>();
            for (Course c : enrolledCourses) {
                enrolledCourseIds.add(c.getCourseId());
            }
            model.addAttribute("enrolledCourseIds", enrolledCourseIds);

            String roleName = user.getRole() != null ? user.getRole().getName() : null;
            model.addAttribute("userRole", roleName);
            boolean isAdmin = "ADMIN".equals(roleName) || "MANAGER".equals(roleName) || "MARKETING".equals(roleName);
            model.addAttribute("isAdmin", isAdmin);
        }

        return "homepage";
    }
}