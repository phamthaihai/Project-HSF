package swt.he182176.hsfproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swt.he182176.hsfproject.dto.LoginDTO;
import swt.he182176.hsfproject.dto.RegisterDTO;
import swt.he182176.hsfproject.entity.Role;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.repository.RoleRepository;
import swt.he182176.hsfproject.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public User login(LoginDTO loginDTO) {
        Optional<User> userOptional = userRepository.findByEmail(loginDTO.getEmail());
        if (userOptional.isEmpty()) {
            return null;
        }
        User user = userOptional.get();

        if(!user.getPassword().equals(loginDTO.getPassword())) {
            return null;
        }

        if(!user.getStatus().equals("Active")) {
            return null;
        }
        return user;
    }

    public User register(RegisterDTO registerDTO) {
        if(userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        User user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setPassword(registerDTO.getPassword());
        user.setPhone(registerDTO.getPhone());
        user.setFullName(registerDTO.getFullname());
        user.setStatus("ACTIVE");

        Role memberRole = roleRepository.findByName("Member");
        user.setRole(memberRole);

        return userRepository.save(user);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

}


