package swt.he182176.hsfproject.controller;

import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.service.UserService;

@Controller
public class ChangePasswordController {
    @Autowired
    UserService userService;

    @PostMapping("/change-password")
    public String changePassword(@RequestParam("oldPassword") String oldPassword,
                                 @RequestParam("newPassword") String newPassword,
                                 @RequestParam("confirmPassword") String confirmPassword,
                                 Model model, HttpSession session) {
        User user = (User)session.getAttribute("user");

        // kiểm tra mật khẩu cũ
        if (!userService.matchesPassword(oldPassword, user.getPasswordHash())) {
            model.addAttribute("error", "Wrong old password");
            return "change-password";
        }

        // kiểm tra confirm password
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Confirm password does not match");
            return "change-password";
        }

        // cập nhật password mới
        user.setPasswordHash(userService.encodePassword(newPassword));
        userService.save(user);

        // cập nhật lại session
        session.setAttribute("user", user);

        model.addAttribute("message", "Change password successful!");
        return "change-password";
    }
}
