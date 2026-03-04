package swt.he182176.hsfproject.dto;

public class ProfileDTO {
    private String userName;
    private String url_img;
    private String email;
    private String phone;
    private boolean active;
    public ProfileDTO() {
    }

    public ProfileDTO(String userName, String url_img, String phone, String email, boolean active) {
        this.userName = userName;
        this.url_img = url_img;
        this.phone = phone;
        this.email = email;
        this.active = active;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUrl_img() {
        return url_img;
    }

    public void setUrl_img(String url_img) {
        this.url_img = url_img;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
