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

        if(user==null){
            return "redirect:/login";
        }

        if(!user.getPassword().equals(oldPassword)){
            model.addAttribute("errol", "Wrong password");
            return "change-password";
        }

        if(!user.getPassword().equals(confirmPassword)){
            model.addAttribute("errol", "Wrong confirm password");
            return "change-password";
        }

        user.setPassword(newPassword);
        userService.save(user);
        model.addAttribute("message","Change Password Successful!");
        return "change-password";
    }
}
