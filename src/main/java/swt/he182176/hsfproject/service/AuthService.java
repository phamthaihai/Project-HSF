package swt.he182176.hsfproject.service;

import jakarta.transaction.Transactional;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import swt.he182176.hsfproject.dto.RegisterDTO;
import swt.he182176.hsfproject.entity.Role;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.entity.UserStatus;
import swt.he182176.hsfproject.repository.RoleRepository;
import swt.he182176.hsfproject.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class AuthService {
    private UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final BCryptPasswordEncoder encoder;

    public AuthService(UserRepository userRepo, RoleRepository roleRepo, BCryptPasswordEncoder encoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.encoder = encoder;
    }

    @Transactional
    public String register(RegisterDTO dto) {
        if (userRepo.existsByEmail(dto.getEmail().trim().toLowerCase())) {
            throw new IllegalArgumentException("Email already exists");
        }

        Role memberRole = roleRepo.findByName("MEMBER")
                .orElseThrow(() -> new IllegalArgumentException("Role MEMBER not found. Check seed role."));

        User u = new User();
        u.setEmail(dto.getEmail().trim().toLowerCase());
        u.setFullName(dto.getFullname().trim());
        u.setPhone(dto.getPhone().trim());
        u.setRole(memberRole);

        u.setPasswordHash(encoder.encode(dto.getPassword()));

        u.setStatus(UserStatus.UNVERIFIED);
        u.setEmailVerified(false);

        String token = UUID.randomUUID().toString().replace("-", "");
        userRepo.save(u);

        return token;
    }

    @Transactional
    public void verifyEmail(String token) {
        User u = userRepo.findByVerifyToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid Email"));

        if (u.getVerifyTokenExpiresAt() == null || u.getVerifyTokenExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token exired");














































        }

        u.setEmailVerified(true);
        u.setStatus(UserStatus.ACTIVE);

        //xóa token ko verify lại
        u.setVerifyToken(null);
        u.setVerifyTokenExpiresAt(null);

        userRepo.save(u);
    }
}
