package swt.he182176.hsfproject.controller;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import swt.he182176.hsfproject.dto.UserDTO;
import swt.he182176.hsfproject.entity.Role;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.entity.UserStatus;
import swt.he182176.hsfproject.service.UserService;

@Controller
@RequestMapping("/users")
public class UserListController {

    @Autowired
    private UserService userService;

    @GetMapping
    public String listUsers(@RequestParam(required = false) String keyword,
                            @RequestParam(required = false) String role,
                            @RequestParam(required = false) UserStatus status,
                            Model model,
                            HttpSession session,
                            RedirectAttributes ra) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            ra.addFlashAttribute("err", "Vui lòng đăng nhập.");
            return "redirect:/login";
        }
        if (currentUser.getRole() == null || currentUser.getRole().getName() == null
                || !currentUser.getRole().getName().equalsIgnoreCase("ADMIN")) {
            ra.addFlashAttribute("err", "Bạn không có quyền truy cập trang này.");
            return "redirect:/user-profile";
        }

        model.addAttribute("users", userService.searchUsers(keyword, role, status));
        model.addAttribute("keyword", keyword);
        model.addAttribute("role", role);
        model.addAttribute("status", status);
        model.addAttribute("statuses", UserStatus.values());
        return "user-list";
    }

    private boolean checkAdmin(HttpSession session, RedirectAttributes ra) {
        User currentUser = (User) session.getAttribute("user");
        if (currentUser == null) {
            ra.addFlashAttribute("err", "Vui lòng đăng nhập.");
            return false;
        }
        if (currentUser.getRole() == null || currentUser.getRole().getName() == null
                || !currentUser.getRole().getName().equalsIgnoreCase("ADMIN")) {
            ra.addFlashAttribute("err", "Bạn không có quyền truy cập trang này.");
            return false;
        }
        return true;
    }

    @GetMapping("/new")
    public String showCreateForm(Model model,
                                 HttpSession session,
                                 RedirectAttributes ra) {
        if (!checkAdmin(session, ra)) {
            return "redirect:/login";
        }
        if (!model.containsAttribute("userDTO")) {
            UserDTO dto = new UserDTO();
            dto.setStatus(UserStatus.ACTIVE);
            dto.setEmailVerified(Boolean.TRUE);
            model.addAttribute("userDTO", dto);
        }
        model.addAttribute("roles", userService.getAllRoles());
        model.addAttribute("statuses", UserStatus.values());
        return "user-detail";
    }

    @GetMapping("/{id}")
    public String showEditForm(@PathVariable Integer id,
                               Model model,
                               HttpSession session,
                               RedirectAttributes ra) {
        if (!checkAdmin(session, ra)) {
            return "redirect:/login";
        }
        if (!model.containsAttribute("userDTO")) {
            model.addAttribute("userDTO", userService.getUserDTOById(id));
        }
        model.addAttribute("roles", userService.getAllRoles());
        model.addAttribute("statuses", UserStatus.values());
        return "user-detail";
    }

    @PostMapping("/save")
    public String saveUser(@Valid @ModelAttribute("userDTO") UserDTO userDTO,
                           BindingResult result,
                           Model model,
                           HttpSession session,
                           RedirectAttributes ra) {
        if (!checkAdmin(session, ra)) {
            return "redirect:/login";
        }
        if (result.hasErrors()) {
            model.addAttribute("roles", userService.getAllRoles());
            model.addAttribute("statuses", UserStatus.values());
            return "user-detail";
        }
        try {
            userService.saveFromAdmin(userDTO);
            ra.addFlashAttribute("msg", "Save user successfully");
            return "redirect:/users";
        } catch (RuntimeException e) {
            model.addAttribute("err", e.getMessage());
            model.addAttribute("roles", userService.getAllRoles());
            model.addAttribute("statuses", UserStatus.values());
            return "user-detail";
        }
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable Integer id,
                             HttpSession session,
                             RedirectAttributes ra) {
        if (!checkAdmin(session, ra)) {
            return "redirect:/login";
        }
        try {
            userService.deleteUser(id);
            ra.addFlashAttribute("msg", "Delete user successfully");
        } catch (RuntimeException e) {
            ra.addFlashAttribute("err", e.getMessage());
        }
        return "redirect:/users";
    }
}

