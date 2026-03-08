package swt.he182176.hsfproject.entity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table (name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="email", nullable=false, unique=true, length = 255)
    private String email;


    @Column(name = "password_hash", nullable = false, length = 255)
    private String passwordHash;

    @Column(name="fullname", nullable=false, length = 100)
    private String fullName;

    @Column(name="phone", length=10, nullable=false)
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserStatus status;

    // token verify email
    @Column(name = "verify_token", length = 64)
    private String verifyToken;

    //hieu luc thoi gian email
    @Column(name = "verify_token_expires_at")
    private LocalDateTime verifyTokenExpiresAt;

    @Column(name = "email_verified", nullable = false)
    private Boolean emailVerified;


    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;


    public User() {}

    public User(int id, String email, LocalDateTime createdAt, Role role, String phone, String fullName, String passwordHash) {
        this.id = id;
        this.email = email;
        this.createdAt = createdAt;
        this.role = role;
        this.phone = phone;
        this.fullName = fullName;
        this.passwordHash = passwordHash;
    }

    @PrePersist
    void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;

        if (this.status == null) this.status =UserStatus.UNVERIFIED;
        if (this.emailVerified ==null) this.emailVerified = false;
    }

    @PreUpdate
    void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public int getId() {
        return id;
    }

    public User(int id, LocalDateTime createdAt, Boolean emailVerified,
                LocalDateTime verifyTokenExpiresAt, String verifyToken, UserStatus status, String passwordHash) {
        this.id = id;
        this.createdAt = createdAt;
        this.emailVerified = emailVerified;
        this.verifyTokenExpiresAt = verifyTokenExpiresAt;
        this.verifyToken = verifyToken;
        this.status = status;
        this.passwordHash = passwordHash;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {this.passwordHash = passwordHash;}

    public UserStatus getStatus() {return status;}

    public void setStatus(UserStatus status) {this.status = status;}

    public String getVerifyToken() {return verifyToken;}

    public void setVerifyToken(String verifyToken) {this.verifyToken = verifyToken;}

    public LocalDateTime getVerifyTokenExpiresAt() {return verifyTokenExpiresAt;}

    public void setVerifyTokenExpiresAt(LocalDateTime verifyTokenExpiresAt) {this.verifyTokenExpiresAt = verifyTokenExpiresAt;}

    public Boolean getEmailVerified() {return emailVerified;}

    public void setEmailVerified(Boolean emailVerified) {
        this.emailVerified = emailVerified;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getUpdatedAt() { return updatedAt; }

}
