package swt.he182176.hsfproject.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.service.PostService;

@Controller
public class HomeController {

    private final PostService postService;

    public HomeController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");

        model.addAttribute("user", user);
        model.addAttribute("latestPosts", postService.getLatestPublishedPosts());

        return "homepage";
    }
}