package swt.he182176.hsfproject.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import swt.he182176.hsfproject.entity.Course;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.repository.CategoryRepository;
import swt.he182176.hsfproject.service.CourseService;

import java.util.List;

@Controller
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CategoryRepository categoryRepository;

    @GetMapping("/public-courses")
    public String showPublicCourses(@RequestParam(required = false) String keyword,
                                    @RequestParam(required = false) Integer categoryId,
                                    Model model) {
        List<Course> courses = courseService.getPublicCourses(keyword, categoryId);

        model.addAttribute("courses", courses);
        model.addAttribute("keyword", keyword);
        model.addAttribute("categoryId", categoryId);
        model.addAttribute("categories", categoryRepository.findByStatusIgnoreCase("ACTIVE"));

        return "public-course";
    }

    @GetMapping("/public-courses/{id}")
    public String showPublicCourseDetail(@PathVariable Integer id, Model model) {
        Course course = courseService.getPublicCourseDetail(id);
        model.addAttribute("course", course);
        return "public-course-detail";
    }

    @GetMapping("/my-courses")
    public String showMyCourses(HttpSession session, Model model) {
        User loginUser = (User) session.getAttribute("user");

        if (loginUser == null) {
            return "redirect:/login";
        }

        List<Course> courses = courseService.getMyCourses(loginUser.getId());
        model.addAttribute("courses", courses);

        return "my-courses";
    }
}