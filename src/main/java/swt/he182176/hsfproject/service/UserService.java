package swt.he182176.hsfproject.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import swt.he182176.hsfproject.dto.LoginDTO;
import swt.he182176.hsfproject.dto.RegisterDTO;
import swt.he182176.hsfproject.entity.Role;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.entity.UserStatus;
import swt.he182176.hsfproject.repository.RoleRepository;
import swt.he182176.hsfproject.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public User login(LoginDTO loginDTO) {
        Optional<User> userOptional = userRepository.findByEmail(loginDTO.getEmail());
        if (userOptional.isEmpty()) {
            return null;
        }
        User user = userOptional.get();


        if(!encoder.matches(loginDTO.getPassword(), user.getPasswordHash())){
            return null;
        }

        if(user.getStatus() != UserStatus.ACTIVE) {
            return null;
        }
        return user;
    }

    public User register(RegisterDTO registerDTO) {
        if(userRepository.existsByEmail(registerDTO.getEmail())) {
            throw new RuntimeException("Email đã tồn tại");
        }

        Role memberRole = roleRepository.findByName("MEMBER")
                .orElseThrow(() -> new RuntimeException("Role MEMBER không tồn tại"));


        User user = new User();
        user.setEmail(registerDTO.getEmail());
        user.setPhone(registerDTO.getPhone());
        user.setFullName(registerDTO.getFullname());
        user.setRole(memberRole);

        // lưu hash
        user.setPasswordHash(encoder.encode(registerDTO.getPassword()));

        // theo spec: register xong -> UNVERIFIED, chưa được login
        user.setStatus(UserStatus.UNVERIFIED);
        user.setEmailVerified(false);

        // theo spec
        user.setStatus(UserStatus.UNVERIFIED);
        user.setEmailVerified(false);

        // token verify (30 phút)
        String token = UUID.randomUUID().toString().replace("-","");
        user.setVerifyToken(token);
        user.setVerifyTokenExpiresAt(LocalDateTime.now().plusMinutes(30));

        return userRepository.save(user);

    }

    @Transactional
    public void verifyEmail(String token){
        User user = userRepository.findByVerifyToken(token)
                .orElseThrow(() -> new RuntimeException("Token khong hop le"));

        if(user.getVerifyTokenExpiresAt() == null || user.getVerifyTokenExpiresAt().isBefore(LocalDateTime.now())){
            throw new RuntimeException("Token đã hết hạn");
        }
        user.setEmailVerified(true);
        user.setStatus(UserStatus.ACTIVE);

        // clear token
        user.setVerifyToken(null);
        user.setVerifyTokenExpiresAt(null);

        userRepository.save(user);
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public String encodePassword(String rawPassword){
        return encoder.encode(rawPassword);
    }

    public boolean matchesPassword(String rawPassword, String passwordHash) {
        return encoder.matches(rawPassword, passwordHash);
    }
}


