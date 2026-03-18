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
        if (user == null) return "redirect:/login";

        // --- BẮT ĐẦU VALIDATION ---

        // 1. Kiểm tra Họ tên (Không trống và từ 3-50 ký tự)
        if (request.getFullName() == null || request.getFullName().trim().length() < 3) {
            ra.addFlashAttribute("err", "Full Name must be at least 3 characters.");
            return "redirect:/enroll/" + request.getCourseId();
        }

        // 2. Kiểm tra Email (Đúng định dạng email)
        String emailRegex = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (request.getEmail() == null || !request.getEmail().matches(emailRegex)) {
            ra.addFlashAttribute("err", "Invalid email format.");
            return "redirect:/enroll/" + request.getCourseId();
        }

        // 3. Kiểm tra Số điện thoại (Định dạng Việt Nam: 10 số, bắt đầu bằng số 0)
        if (request.getMobile() == null || !request.getMobile().matches("^0[0-9]{9}$")) {
            ra.addFlashAttribute("err", "Mobile must be 10 digits and start with 0.");
            return "redirect:/enroll/" + request.getCourseId();
        }

        // 4. Kiểm tra Phương thức thanh toán
        if (request.getPaymentMethod() == null || request.getPaymentMethod().isEmpty()) {
            ra.addFlashAttribute("err", "Please select a payment method.");
            return "redirect:/enroll/" + request.getCourseId();
        }

        // --- KẾT THÚC VALIDATION ---

        try {
            // Gọi service xử lý (Nên cập nhật bản ghi cũ nếu trạng thái là PENDING/REJECTED)
            Enrollment enrollment = enrollmentService.createEnrollment(request, user);

            if (enrollment.getEnrollmentId() == null) {
                throw new RuntimeException("Error: Could not generate Enrollment ID.");
            }

            String method = request.getPaymentMethod();
            if ("PAYOS".equalsIgnoreCase(method)) {
                return "redirect:/payment/payos/" + enrollment.getEnrollmentId();
            } else if ("VNPAY".equalsIgnoreCase(method)) {
                return "redirect:/payment/vnpay/" + enrollment.getEnrollmentId();
            }

            return "redirect:/public-courses?msg=success";

        } catch (Exception e) {
            ra.addFlashAttribute("err", e.getMessage());
            return "redirect:/enroll/" + request.getCourseId();
        }
    }
    @GetMapping("/enroll/edit/{enrollmentId}")
    public String showReEnrollForm(@PathVariable Integer enrollmentId,
                                   HttpSession session,
                                   Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        // Tìm đơn đăng ký cũ
        Enrollment enrollment = enrollmentService.getEnrollmentById(enrollmentId);

        // Bảo mật: Chỉ cho phép chính chủ xem đơn của mình
        if (!enrollment.getUser().getId().equals(user.getId())) {
            return "redirect:/my-enrollments";
        }

        // Đưa dữ liệu vào model
        model.addAttribute("course", enrollment.getCourse());
        model.addAttribute("user", user);
        model.addAttribute("oldEnrollment", enrollment); // Để hiển thị Rejection Note nếu có

        return "enroll"; // Dùng chung trang enroll.html
    }
}