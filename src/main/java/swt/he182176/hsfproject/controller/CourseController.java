package swt.he182176.hsfproject.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import swt.he182176.hsfproject.entity.Course;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.service.CourseService;

import java.util.List;

@Controller
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/public-courses")
    public String showPublicCourses(@RequestParam(required = false) String keyword, Model model) {
        List<Course> courses = courseService.getPublicCourses(keyword);
        model.addAttribute("courses", courses);
        model.addAttribute("keyword", keyword);
        return "public-courses";
    }

    @GetMapping("/my-courses")
    public String showMyCourses(
            @RequestParam(required = false) Integer userId,
            HttpSession session,
            Model model
    ) {
        User loginUser = (User) session.getAttribute("user");

        Integer targetUserId = null;
        if (loginUser != null) {
            targetUserId = loginUser.getId();
        } else if (userId != null) {
            targetUserId = userId;
        }

        if (targetUserId == null) {
            model.addAttribute("err", "Chưa có user để xem My Courses. Tạm thời test bằng /my-courses?userId=1");
            return "my-courses";
        }

        List<Course> courses = courseService.getMyCourses(targetUserId);
        model.addAttribute("courses", courses);
        model.addAttribute("testUserId", targetUserId);
        return "my-courses";
    }
}