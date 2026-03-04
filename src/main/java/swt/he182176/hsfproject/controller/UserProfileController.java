package swt.he182176.hsfproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import swt.he182176.hsfproject.dto.ProfileDTO;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.service.UserService;

@Controller
public class UserProfileController {
    @Autowired
    UserService userService;

    @GetMapping("/list")
    public String showUserProfile(){
        return "list";
    }

    @GetMapping("/user-profile-details")
    public String showUserProfileForm(){
        return "user-profile-details";
    }

    @PostMapping("/user-profile-details")
    public String userProfileDetails(){
        User user = new User();
        ProfileDTO profileDTO = new ProfileDTO();
        user.setFullName(profileDTO.getUserName());
        user.setEmail(profileDTO.getEmail());
        user.setPhone((profileDTO.getPhone()));
        userService.save(user);
        return "redirect:/list";
    }
}
