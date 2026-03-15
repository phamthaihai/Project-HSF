package swt.he182176.hsfproject.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import swt.he182176.hsfproject.dto.LoginDTO;
import swt.he182176.hsfproject.dto.ProfileDTO;
import swt.he182176.hsfproject.dto.RegisterDTO;
import swt.he182176.hsfproject.entity.Role;
import swt.he182176.hsfproject.entity.User;
import swt.he182176.hsfproject.entity.UserStatus;
import swt.he182176.hsfproject.repository.RoleRepository;
import swt.he182176.hsfproject.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
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
            // Fallback for manually seeded plain-text passwords.
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
        return encoder.matches(rawPassword, passwordHash);
    }

    public Optional<User> findById(Integer id) {
        return userRepository.findById(id);
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

    @Transactional
    public User updateProfile(Integer userId, ProfileDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (userRepository.existsByEmailAndIdNot(dto.getEmail(), userId)) {
            throw new RuntimeException("Email already exists");
        }

        user.setFullName(dto.getFullName());
        user.setEmail(dto.getEmail());
        user.setPhone(dto.getPhone());

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

        user.setPasswordHash(encodePassword(newPassword));
        userRepository.save(user);
    }
}
