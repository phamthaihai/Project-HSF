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
import swt.he182176.hsfproject.dto.LoginDTO;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.service.UserService;

@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @GetMapping("/login")
    public String showFormLogin(@RequestParam(value = "msg", required = false) String msg,
                                Model model) {
        model.addAttribute("loginDTO", new LoginDTO());

        if ("passwordChanged".equals(msg)) {
            model.addAttribute("success", "Password changed successfully. Please login again.");
        }

        return "login";
    }

    @PostMapping("/login")
    public String loginUser(@Valid @ModelAttribute("loginDTO") LoginDTO loginDTO,
                            BindingResult result,
                            Model model,
                            HttpSession session) {
        if (result.hasErrors()) {
            return "login";
        }

        User user = userService.login(loginDTO);

        if (user == null) {
            model.addAttribute("error", "Invalid email or password");
            return "login";
        }

        session.setAttribute("user", user);
        return "redirect:/blogs";
    }
}