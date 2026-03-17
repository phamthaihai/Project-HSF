package swt.he182176.hsfproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import swt.he182176.hsfproject.entity.Enrollment;
import swt.he182176.hsfproject.service.CourseService;
import swt.he182176.hsfproject.service.EnrollmentService;
import swt.he182176.hsfproject.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin/enrollments")
public class AdminEnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;
    @Autowired
    private CourseService courseService; // Thêm dòng này

    @Autowired
    private UserService userService;

    // LIST
    @GetMapping
    public String list(
            @RequestParam(required = false) Integer courseId,
            @RequestParam(required = false) Integer userId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            Model model) {

        // Lấy danh sách đã lọc
        List<Enrollment> enrollments = enrollmentService.filterEnrollments(courseId, userId, status, keyword);
        model.addAttribute("enrollments", enrollments);

        // QUAN TRỌNG: Nạp danh sách cho dropdown
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("users", userService.getAllUsers()); // Đảm bảo hàm này trả về danh sách User

        return "admin/enrollment-list";
    }

    // DETAILS
    @GetMapping("/{id}")
    public String details(@PathVariable Integer id, Model model) {

        Enrollment enrollment = enrollmentService.getEnrollmentById(id);

        model.addAttribute("enrollment", enrollment);

        return "admin/enrollment-details";
    }

    // APPROVE
    @PostMapping("/{id}/approve")
    public String approve(@PathVariable Integer id) {

        enrollmentService.approveEnrollment(id);

        return "redirect:/admin/enrollments";
    }

    // REJECT
    @PostMapping("/{id}/reject")
    public String reject(@PathVariable Integer id,
                         @RequestParam String note) {

        enrollmentService.rejectEnrollment(id, note);

        return "redirect:/admin/enrollments";
    }


    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") Integer id) {
        enrollmentService.deleteEnrollment(id);
        return "redirect:/admin/enrollments";
    }

    @PostMapping("/update")
    public String updateEnrollment(
            @RequestParam int id,
            @RequestParam String fullName,
            @RequestParam String status,
            @RequestParam String note,
            @RequestParam(required = false) String rejectedNote){

        Enrollment e = enrollmentService.findById(id);

        e.setFullName(fullName);
        e.setStatus(status);
        e.setNote(note);
        e.setRejectedNote(rejectedNote);
        e.setUpdatedAt(LocalDateTime.now());

        enrollmentService.save(e);

        return "redirect:/admin/enrollments";
    }
}