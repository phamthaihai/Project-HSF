package swt.he182176.hsfproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import swt.he182176.hsfproject.entity.Enrollment;
import swt.he182176.hsfproject.service.EnrollmentService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin/enrollments")
public class AdminEnrollmentController {

    @Autowired
    private EnrollmentService enrollmentService;

    // LIST
    @GetMapping
    public String list(Model model) {

        List<Enrollment> enrollments = enrollmentService.getAllEnrollments();

        model.addAttribute("enrollments", enrollments);

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

    // DELETE
    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Integer id) {

        enrollmentService.deleteEnrollment(id);

        return "redirect:/admin/enrollments";
    }
    // API UPDATE
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