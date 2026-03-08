package swt.he182176.hsfproject.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import swt.he182176.hsfproject.dto.RegisterDTO;
import swt.he182176.hsfproject.service.AuthService;

@Controller
public class RegisterController {

    private final AuthService authService;

    public RegisterController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/register")
    public String showRegister(Model model){
        model.addAttribute("registerDTO", new RegisterDTO());
        return "register";
    }

    @PostMapping("/register")
    public String doRegister(
            @Valid @ModelAttribute("registerDTO") RegisterDTO dto,
            BindingResult br,
            RedirectAttributes ra,
            HttpServletRequest request
    ){
            if (br.hasErrors()) return "register";
            try{
                String token = authService.register(dto);
                String baseUrl = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
                String verifyLink = baseUrl + "/verify?token=" + token;

                System.out.println("Verify Link: " + verifyLink);

                ra.addFlashAttribute("msg"
                        ,"Register success! Please check email to verify. (See console verify link)");
                return "redirect:/login";
            } catch (Exception e) {
               ra.addFlashAttribute("err", e.getMessage());
               return "redirect:/register";
            }

    }
    @GetMapping("/verify")
    public String verify(@RequestParam("token") String token, RedirectAttributes ra){
        try{
            authService.verifyEmail(token);
            ra.addFlashAttribute("msg", "Email verified successfully. You can login now");
            return "redirect:/login";
        } catch (Exception e){
            ra.addFlashAttribute("msg", e.getMessage());
            return "redirect:/login";
        }
    }
}
