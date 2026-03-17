package swt.he182176.hsfproject.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import swt.he182176.hsfproject.dto.ForgotPasswordDTO;
import swt.he182176.hsfproject.dto.LoginDTO;
import swt.he182176.hsfproject.dto.ProfileDTO;
import swt.he182176.hsfproject.dto.RegisterDTO;
import swt.he182176.hsfproject.dto.ResetPasswordDTO;
import swt.he182176.hsfproject.dto.UserDTO;
import swt.he182176.hsfproject.entity.Role;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.entity.UserStatus;
import swt.he182176.hsfproject.repository.RoleRepository;
import swt.he182176.hsfproject.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder encoder;

    public User login(LoginDTO loginDTO) {
        if (loginDTO == null || loginDTO.getEmail() == null || loginDTO.getPassword() == null) {
            return null;
        }

        String email = loginDTO.getEmail().trim().toLowerCase();
        Optional<User> userOptional = userRepository.findByEmailIgnoreCase(email);
        if (userOptional.isEmpty()) {
            return null;
        }

        User user = userOptional.get();

        String rawPassword = loginDTO.getPassword();
        String stored = user.getPasswordHash();
        if (stored == null) {
            return null;
        }

        stored = stored.trim();
        if (stored.length() >= 2 && stored.startsWith("'") && stored.endsWith("'")) {
            stored = stored.substring(1, stored.length() - 1).trim();
        }

        boolean passwordOk = false;
        try {
            passwordOk = encoder.matches(rawPassword, stored);
        } catch (Exception ignored) {
        }

        if (!passwordOk) {
            passwordOk = rawPassword.equals(stored);
        }

        if (!passwordOk || user.getStatus() != UserStatus.ACTIVE) {
            return null;
        }

        return user;
    }

    public User save(User user) {
        return userRepository.save(user);
    }

    public String encodePassword(String rawPassword) {
        return encoder.encode(rawPassword);
    }

    public boolean matchesPassword(String rawPassword, String passwordHash) {
        if (passwordHash == null || rawPassword == null) {
            return false;
        }

        String stored = passwordHash.trim();
        if (stored.length() >= 2 && stored.startsWith("'") && stored.endsWith("'")) {
            stored = stored.substring(1, stored.length() - 1).trim();
        }

        try {
            if (encoder.matches(rawPassword, stored)) {
                return true;
            }
        } catch (Exception ignored) {
        }

        return rawPassword.equals(stored);
    }

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public List<User> searchUsers(String keyword, String roleName, UserStatus status) {
        List<User> base;
        if (keyword == null || keyword.trim().isEmpty()) {
            base = userRepository.findAll();
        } else {
            String kw = keyword.trim();
            base = userRepository.findByEmailContainingIgnoreCaseOrFullNameContainingIgnoreCase(kw, kw);
        }

        return base.stream()
                .filter(u -> {
                    if (roleName == null || roleName.trim().isEmpty()) {
                        return true;
                    }
                    if (u.getRole() == null || u.getRole().getName() == null) {
                        return false;
                    }
                    return u.getRole().getName().equalsIgnoreCase(roleName.trim());
                })
                .filter(u -> status == null || u.getStatus() == status)
                .sorted(Comparator.comparingInt(User::getId))
                .collect(Collectors.toList());
    }

    public UserDTO getUserDTOById(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserDTO dto = new UserDTO();
        dto.setId(user.getId());
        dto.setFullName(user.getFullName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRoleId(user.getRole() != null ? user.getRole().getId() : null);
        dto.setStatus(user.getStatus());
        dto.setEmailVerified(user.getEmailVerified());
        return dto;
    }

    public void saveFromAdmin(UserDTO dto) {
        if (dto.getId() == null) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw new RuntimeException("Email already exists");
            }
        } else {
            if (userRepository.existsByEmailAndIdNot(dto.getEmail(), dto.getId())) {
                throw new RuntimeException("Email already exists");
            }
        }

        Role role = roleRepository.findById(dto.getRoleId())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        User user = dto.getId() == null
                ? new User()
                : userRepository.findById(dto.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());
        user.setRole(role);

        if (dto.getStatus() != null) {
            user.setStatus(dto.getStatus());
        }
        if (dto.getEmailVerified() != null) {
            user.setEmailVerified(dto.getEmailVerified());
        }

        if (dto.getId() == null) {
            if (dto.getPassword() == null || dto.getPassword().length() < 6) {
                throw new RuntimeException("Password must be at least 6 characters for new user");
            }
            user.setPasswordHash(encodePassword(dto.getPassword()));
        } else if (dto.getPassword() != null && !dto.getPassword().isBlank()) {
            user.setPasswordHash(encodePassword(dto.getPassword()));
        }

        if (user.getStatus() == null) {
            user.setStatus(UserStatus.ACTIVE);
        }
        if (user.getEmailVerified() == null) {
            user.setEmailVerified(Boolean.TRUE);
        }

        userRepository.save(user);
    }

    @Transactional
    public void deleteUser(Integer id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        userRepository.delete(user);
    }

    @Transactional
    public String createResetPasswordOtp(ForgotPasswordDTO dto) {
        String email = dto.getEmail().trim().toLowerCase();
        Optional<User> opt = userRepository.findByEmailIgnoreCase(email);
        if (opt.isEmpty()) {
            throw new RuntimeException("Email not found");
        }

        User user = opt.get();
        String otp = String.format("%06d", (int) (Math.random() * 1_000_000));
        user.setVerifyToken(otp);
        user.setVerifyTokenExpiresAt(LocalDateTime.now().plusMinutes(10));
        userRepository.save(user);
        return otp;
    }

    @Transactional
    public void resetPassword(ResetPasswordDTO dto) {
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("Confirm password does not match");
        }

        String email = dto.getEmail().trim().toLowerCase();
        String otp = dto.getOtp().trim();

        User user = userRepository.findByEmailIgnoreCase(email)
                .filter(u -> otp.equals(u.getVerifyToken()))
                .orElseThrow(() -> new RuntimeException("Invalid email or OTP"));

        if (user.getVerifyTokenExpiresAt() == null
                || user.getVerifyTokenExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("OTP has expired");
        }

        user.setPasswordHash(encodePassword(dto.getNewPassword()));
        user.setVerifyToken(null);
        user.setVerifyTokenExpiresAt(null);
        userRepository.save(user);
    }

    @Transactional
    public User updateProfile(Integer userId, ProfileDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String fullName = dto.getFullName() == null ? "" : dto.getFullName().trim();
        String email = dto.getEmail() == null ? "" : dto.getEmail().trim().toLowerCase();
        String phone = dto.getPhone() == null ? "" : dto.getPhone().trim();

        if (userRepository.existsByEmailAndIdNot(email, userId)) {
            throw new RuntimeException("Email already exists");
        }

        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);

        return userRepository.save(user);
    }

    @Transactional
    public void changePassword(Integer userId, String oldPassword, String newPassword, String confirmPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!matchesPassword(oldPassword, user.getPasswordHash())) {
            throw new RuntimeException("Wrong current password");
        }

        if (!newPassword.equals(confirmPassword)) {
            throw new RuntimeException("Confirm password does not match");
        }

        if (matchesPassword(newPassword, user.getPasswordHash())) {
            throw new RuntimeException("New password must be different from current password");
        }

        user.setPasswordHash(encodePassword(newPassword));
        userRepository.save(user);
    }
    @Autowired
    private FileStorageService fileStorageService;

    @Transactional
    public User updateProfile(Integer userId, ProfileDTO dto, org.springframework.web.multipart.MultipartFile avatarFile) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String fullName = dto.getFullName() == null ? "" : dto.getFullName().trim();
        String email = dto.getEmail() == null ? "" : dto.getEmail().trim().toLowerCase();
        String phone = dto.getPhone() == null ? "" : dto.getPhone().trim();

        if (userRepository.existsByEmailAndIdNot(email, userId)) {
            throw new RuntimeException("Email already exists");
        }

        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);

        if (avatarFile != null && !avatarFile.isEmpty()) {
            String avatarPath = fileStorageService.saveAvatar(avatarFile);
            user.setAvatar(avatarPath);
        }

        return userRepository.save(user);
    }
}