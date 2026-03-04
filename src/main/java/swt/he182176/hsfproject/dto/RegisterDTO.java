package swt.he182176.hsfproject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class RegisterDTO {
    @NotBlank(message = "Fullname is required")
    private String fullname;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits")
    private String phone;

    @NotBlank(message = "Password is reuqired")
    @Size(min = 7, message = "Password must be greater than 6 characters")
    private String password;

    public @NotBlank(message = "Fullname is required") String getFullname() {
        return fullname;
    }

    public void setFullname(@NotBlank(message = "Fullname is required") String fullname) {
        this.fullname = fullname;
    }

    public @NotBlank(message = "Password is reuqired") @Size(min = 7, message = "Password must be greater than 6 characters") String getPassword() {
        return password;
    }

    public void setPassword(@NotBlank(message = "Password is reuqired") @Size(min = 7, message = "Password must be greater than 6 characters") String password) {
        this.password = password;
    }

    public @NotBlank(message = "Email is required") @Email(message = "Invalid email format") String getEmail() {
        return email;
    }

    public void setEmail(@NotBlank(message = "Email is required") @Email(message = "Invalid email format") String email) {
        this.email = email;
    }

    public @NotBlank(message = "Phone is required") @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits") String getPhone() {
        return phone;
    }

    public void setPhone(@NotBlank(message = "Phone is required") @Pattern(regexp = "^[0-9]{10}$", message = "Phone must be 10 digits") String phone) {
        this.phone = phone;
    }
}
