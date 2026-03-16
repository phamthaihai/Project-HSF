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

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final BCryptPasswordEncoder encoder;
    private final EmailService emailService;

    public AuthService(UserRepository userRepo,
                       RoleRepository roleRepo,
                       BCryptPasswordEncoder encoder,
                       EmailService emailService) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.encoder = encoder;
        this.emailService = emailService;
    }

    @Transactional
    public void register(RegisterDTO dto, String baseUrl) {

        String email = dto.getEmail().trim().toLowerCase();

        if (userRepo.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        Role memberRole = roleRepo.findByName("MANAGER")
                .orElseThrow(() -> new IllegalArgumentException("Role MEMBER not found. Check seed role."));

        String token = UUID.randomUUID().toString().replace("-", "");

        User user = new User();
        user.setEmail(email);
        user.setFullName(dto.getFullname().trim());
        user.setPhone(dto.getPhone() != null ? dto.getPhone().trim() : null);
        user.setRole(memberRole);
        user.setPasswordHash(encoder.encode(dto.getPassword()));
        user.setStatus(UserStatus.UNVERIFIED);
        user.setEmailVerified(false);
        user.setVerifyToken(token);
        user.setVerifyTokenExpiresAt(LocalDateTime.now().plusMinutes(30));

        userRepo.save(user);

        String verifyLink = baseUrl + "/verify?token=" + token;
        emailService.sendVerificationEmail(user.getEmail(), user.getFullName(), verifyLink);
    }

    @Transactional
    public void verifyEmail(String token) {
        User user = userRepo.findByVerifyToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid verification token."));

        if (user.getVerifyTokenExpiresAt() == null
                || user.getVerifyTokenExpiresAt().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Verification token has expired.");
        }

        user.setEmailVerified(true);
        user.setStatus(UserStatus.ACTIVE);
        user.setVerifyToken(null);
        user.setVerifyTokenExpiresAt(null);

        userRepo.save(user);
    }
}