package swt.he182176.hsfproject.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import swt.he182176.hsfproject.dto.LessonViewerDTO;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.service.LessonService;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @GetMapping("/my-courses/{courseId}/learn")
    public String showLessonViewer(@PathVariable Integer courseId,
                                   @RequestParam(required = false) Integer lessonId,
                                   HttpSession session,
                                   Model model) {

        User loginUser = (User) session.getAttribute("user");

        if (loginUser == null) {
            return "redirect:/login";
        }

        try {
            LessonViewerDTO viewer = lessonService.getLessonViewer(loginUser, courseId, lessonId);
            model.addAttribute("viewer", viewer);
            return "lesson-viewer";
        } catch (RuntimeException e) {
            String encodedMessage = URLEncoder.encode(e.getMessage(), StandardCharsets.UTF_8);
            return "redirect:/my-courses?err=" + encodedMessage;
        }
    }
}