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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import swt.he182176.hsfproject.dto.LoginDTO;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.service.UserService;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showFormLogin(@ModelAttribute("msg") String msg,
                                @ModelAttribute("err") String err,
                                Model model,
                                HttpSession session) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser != null) {
            return "redirect:/user-profile";
        }

        if (!model.containsAttribute("loginDTO")) {
            model.addAttribute("loginDTO", new LoginDTO());
        }

        if (msg != null && !msg.isBlank()) {
            model.addAttribute("success", msg);
        }
        if (err != null && !err.isBlank()) {
            model.addAttribute("error", err);
        }

        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@Valid @ModelAttribute("loginDTO") LoginDTO loginDTO,
                            BindingResult result,
                            Model model,
                            HttpSession session,
                            RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "login";
        }

        User user = userService.login(loginDTO);

        if (user == null) {
            model.addAttribute("error", "Invalid email, password, or your email has not been verified.");
            return "login";
        }

        session.setAttribute("user", user);
        ra.addFlashAttribute("msg", "Login successful");
        return "redirect:/user-profile";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes ra) {
        session.invalidate();
        ra.addFlashAttribute("msg", "You have logged out");
        return "redirect:/login";
    }
}
