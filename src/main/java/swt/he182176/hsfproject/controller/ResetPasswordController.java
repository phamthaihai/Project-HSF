package swt.he182176.hsfproject.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import swt.he182176.hsfproject.dto.ForgotPasswordDTO;
import swt.he182176.hsfproject.dto.ResetPasswordDTO;
import swt.he182176.hsfproject.service.MailService;
import swt.he182176.hsfproject.service.UserService;

@Controller
public class ResetPasswordController {

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm(Model model) {
        if (!model.containsAttribute("forgotPasswordDTO")) {
            model.addAttribute("forgotPasswordDTO", new ForgotPasswordDTO());
        }
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String handleForgotPassword(@Valid @ModelAttribute("forgotPasswordDTO") ForgotPasswordDTO dto,
                                       BindingResult result,
                                       Model model,
                                       RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "forgot-password";
        }
        try {
            String otp = userService.createResetPasswordOtp(dto);
            String subject = "Your password reset OTP";
            String body = "Your OTP code is: " + otp + "\nThis code will expire in 10 minutes.";
            mailService.sendSimpleMail(dto.getEmail(), subject, body);

            ra.addFlashAttribute("msg",
                    "If this email exists, an OTP has been sent.");
            return "redirect:/reset-password";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "forgot-password";
        }
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(Model model) {
        if (!model.containsAttribute("resetPasswordDTO")) {
            model.addAttribute("resetPasswordDTO", new ResetPasswordDTO());
        }
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String handleResetPassword(@Valid @ModelAttribute("resetPasswordDTO") ResetPasswordDTO dto,
                                      BindingResult result,
                                      Model model,
                                      RedirectAttributes ra) {
        if (result.hasErrors()) {
            return "reset-password";
        }
        try {
            userService.resetPassword(dto);
            ra.addFlashAttribute("msg", "Password reset successfully. You can login now.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            model.addAttribute("error", e.getMessage());
            return "reset-password";
        }
    }
}

