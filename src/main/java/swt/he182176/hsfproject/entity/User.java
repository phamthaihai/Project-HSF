package swt.he182176.hsfproject.entity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table (name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name="email", nullable=false, unique=true)
    private String email;

    @Column(name="password", nullable=false)
    private String password;

    @Column(name="fullname", nullable=false)
    private String fullName;

    @Column(name="phone", length=10, nullable=false)
    private String phone;

    @ManyToOne
    @JoinColumn(name="role_id")
    private Role role;

    @Column(name = "status")
    private String status;

    @Column(name = "is_verified")
    private Boolean isVerified;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


    public User() {}

    public User(int id, String email, LocalDateTime createdAt, Boolean isVerified, String status, Role role, String phone, String fullName, String password) {
        this.id = id;
        this.email = email;
        this.createdAt = createdAt;
        this.isVerified = isVerified;
        this.status = status;
        this.role = role;
        this.phone = phone;
        this.fullName = fullName;
        this.password = password;
    }


    public int getId() {
        return id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getVerified() {
        return isVerified;
    }

    public void setVerified(Boolean verified) {
        isVerified = verified;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
