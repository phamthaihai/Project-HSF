package swt.he182176.hsfproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import swt.he182176.hsfproject.service.CourseAdminService;

import java.util.Map;

@Controller
@RequestMapping("/admin")
public class AdminDashboardController {

    @Autowired
    private CourseAdminService courseAdminService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        var data = courseAdminService.getDashboardData();
        model.addAllAttributes(data);

        return "admin-dashboard";
    }


   @GetMapping("/dashboard")
   public String showDashboard(Model model){

       Map<String, Object> data = courseAdminService.getDashboardData();
       model.addAttribute("totalUsers", data.get("totalUsers"));
       model.addAttribute("totalCourses", data.get("totalCourses"));
       model.addAttribute("publishedCourses", data.get("publishedCourses"));
       model.addAttribute("publishedPosts", data.get("publishedPosts"));
       model.addAttribute("recentCourses", data.get("recentCourses"));

        return "admin-dashboard";
   }
}

