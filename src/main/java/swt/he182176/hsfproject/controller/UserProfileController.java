package swt.he182176.hsfproject.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import swt.he182176.hsfproject.dto.ProfileDTO;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.service.UserService;

@Controller
public class UserProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/user-profile")
    public String showUserProfileForm(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        ProfileDTO dto = new ProfileDTO(
                user.getFullName(),
                user.getEmail(),
                user.getPhone()
        );

        model.addAttribute("profileDTO", dto);
        return "user-profile";
    }

    @PostMapping("/user-profile")
    public String updateUserProfile(@Valid ProfileDTO profileDTO,
                                    BindingResult result,
                                    Model model,
                                    HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            return "user-profile";
        }

        try {
            User updatedUser = userService.updateProfile(currentUser.getId(), profileDTO);
            session.setAttribute("user", updatedUser);
            model.addAttribute("success", "Profile updated successfully");
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
        }

        return "user-profile";
    }
}