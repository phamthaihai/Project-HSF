package swt.he182176.hsfproject.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import swt.he182176.hsfproject.dto.ChangePasswordDTO;
import swt.he182176.hsfproject.dto.ProfileDTO;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.service.UserService;

@Controller
public class UserProfileController {

    @Autowired
    private UserService userService;

    @GetMapping("/user-profile")
    public String showUserProfileForm(@RequestParam(value = "tab", required = false, defaultValue = "profile") String tab,
                                      Model model,
                                      HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        if (!model.containsAttribute("profileDTO")) {
            ProfileDTO profileDTO = new ProfileDTO(
                    user.getFullName(),
                    user.getEmail(),
                    user.getPhone(),
                    user.getAvatar()
            );
            model.addAttribute("profileDTO", profileDTO);
        }

        if (!model.containsAttribute("changePasswordDTO")) {
            model.addAttribute("changePasswordDTO", new ChangePasswordDTO());
        }

        model.addAttribute("currentUser", user);
        model.addAttribute("activeTab", tab);
        return "user-profile";
    }

    @PostMapping("/user-profile")
    public String updateUserProfile(@Valid @ModelAttribute("profileDTO") ProfileDTO profileDTO,
                                    BindingResult result,
                                    @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile,
                                    HttpSession session,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            return "redirect:/login";
        }

        if (result.hasErrors()) {
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("changePasswordDTO", new ChangePasswordDTO());
            model.addAttribute("activeTab", "profile");
            return "user-profile";
        }

        try {
            User updatedUser = userService.updateProfile(currentUser.getId(), profileDTO, avatarFile);
            session.setAttribute("user", updatedUser);
            redirectAttributes.addFlashAttribute("success", "Profile updated successfully");
            return "redirect:/user-profile?tab=profile";
        } catch (RuntimeException e) {
            model.addAttribute("currentUser", currentUser);
            model.addAttribute("changePasswordDTO", new ChangePasswordDTO());
            model.addAttribute("activeTab", "profile");
            model.addAttribute("error", e.getMessage());
            return "user-profile";
        }
    }
}