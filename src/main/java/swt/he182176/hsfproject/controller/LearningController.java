package swt.he182176.hsfproject.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import swt.he182176.hsfproject.dto.LessonViewerDTO;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.service.LessonService;

@Controller
@RequestMapping("/my-courses")
public class LearningController {

    @Autowired
    private LessonService lessonService;

    @GetMapping("/{courseId}/learn")
    public String showLessonViewer(
            @PathVariable Integer courseId,
            @RequestParam(required = false) Integer lessonId,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            LessonViewerDTO viewerDTO = lessonService.getLessonViewer(user, courseId, lessonId);
            model.addAttribute("viewer", viewerDTO);
            return "lesson-viewer";

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("err", e.getMessage());
            return "redirect:/my-courses";
        }
    }
}
