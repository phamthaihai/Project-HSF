package swt.he182176.hsfproject.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import swt.he182176.hsfproject.entity.Category;
import swt.he182176.hsfproject.entity.Course;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.repository.CategoryRepository;
import swt.he182176.hsfproject.repository.UserRepository;
import swt.he182176.hsfproject.service.CourseAdminService;

import java.time.LocalDateTime;
import java.util.Map;

@Controller
@RequestMapping("/courses")
public class CourseAdminController {

    @Autowired
    private CourseAdminService courseService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dashboard")
    public String showDashboard(Model model) {
        Map<String, Object> data = courseService.getDashboardData();

        model.addAttribute("totalUsers", data.get("totalUsers"));
        model.addAttribute("totalCourses", data.get("totalCourses"));
        model.addAttribute("publishedCourses", data.get("publishedCourses"));
        model.addAttribute("publishedPosts", data.get("publishedPosts"));
        model.addAttribute("recentCourses", data.get("recentCourses"));

        return "admin-dashboard";
    }

    @GetMapping
    public String showCourseList(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "course-list";
    }

    @GetMapping("/add")
    public String showAddCourse(Model model, HttpSession session) {
        Course course = new Course();

        User loginUser = (User) session.getAttribute("user");

        if (loginUser != null
                && loginUser.getRole() != null
                && "MANAGER".equalsIgnoreCase(loginUser.getRole().getName())) {
            course.setInstructor(loginUser);
        } else {
            course.setInstructor(new User());
        }

        course.setCategory(new Category());

        model.addAttribute("course", course);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("managers", userRepository.findByRole_Name("MANAGER"));

        return "course-detail";
    }

    @GetMapping("/edit/{id}")
    public String showEditCourse(@PathVariable int id, Model model) {
        Course course = courseService.getCourseById(id);

        model.addAttribute("course", course);
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("managers", userRepository.findByRole_Name("MANAGER"));

        return "course-detail";
    }

    @PostMapping("/save")
    public String saveCourse(@ModelAttribute Course course,
                             @RequestParam(required = false) Integer categoryId,
                             @RequestParam(required = false) Integer instructorId,
                             @RequestParam(defaultValue = "false") boolean published,
                             HttpSession session) {

        Course oldCourse = null;
        if (course.getCourseId() != null) {
            oldCourse = courseService.getCourseById(course.getCourseId());
        }

        if (oldCourse != null && oldCourse.getCreateAt() != null) {
            course.setCreateAt(oldCourse.getCreateAt());
        } else {
            course.setCreateAt(LocalDateTime.now());
        }

        if (categoryId != null) {
            Category category = categoryRepository.findById(categoryId).orElse(null);
            course.setCategory(category);
        } else {
            course.setCategory(null);
        }

        if (instructorId != null) {
            User instructor = userRepository.findById(instructorId).orElse(null);
            course.setInstructor(instructor);
        } else {
            User loginUser = (User) session.getAttribute("user");
            if (loginUser != null
                    && loginUser.getRole() != null
                    && "MANAGER".equalsIgnoreCase(loginUser.getRole().getName())) {
                course.setInstructor(loginUser);
            } else {
                course.setInstructor(null);
            }
        }

        course.setPublished(published);

        courseService.updateCourse(course);

        return "redirect:/courses";
    }

    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable int id) {
        courseService.deleteCourse(id);
        return "redirect:/courses";
    }
}