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
            if (currentUser.getRole() != null
                    && "ADMIN".equalsIgnoreCase(currentUser.getRole().getName())) {
                return "redirect:/admin/dashboard";
            }
            return "redirect:/";
        }

        // Khởi tạo form nếu chưa có
        if (!model.containsAttribute("loginDTO")) {
            model.addAttribute("loginDTO", new LoginDTO());
        }

        // Map flash attribute từ đăng ký/verify sang message hiển thị trên login.html
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
            model.addAttribute("error", "Email hoặc mật khẩu không đúng hoặc tài khoản chưa được kích hoạt");
            return "login";
        }

        // Lưu user vào session
        session.setAttribute("user", user);
        ra.addFlashAttribute("msg", "Đăng nhập thành công");

        // Điều hướng theo role sau khi đăng nhập
        if (user.getRole() != null
                && "ADMIN".equalsIgnoreCase(user.getRole().getName())) {
            return "redirect:/admin/dashboard";
        }
        return "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes ra) {
        session.invalidate();
        ra.addFlashAttribute("msg", "Bạn đã đăng xuất");
        return "redirect:/login";
    }
}
