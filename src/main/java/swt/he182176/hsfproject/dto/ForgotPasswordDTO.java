package swt.he182176.hsfproject.dto;

public class ForgotPasswordDTO {

    private String email;

    public ForgotPasswordDTO() {
    }

    public ForgotPasswordDTO(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}