package swt.he182176.hsfproject.dto;

public class EnrollRequest {

    // Nên dùng Integer thay vì int
    private Integer courseId;

    private String fullName;
    private String email;
    private String mobile;
    private String note;
    private String paymentMethod;

    // Cập nhật Getter/Setter sang Integer
    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    // Các Getter và Setter còn lại giữ nguyên...
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }
    public String getNote() { return note; }
    public void setNote(String note) { this.note = note; }
    public String getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(String paymentMethod) { this.paymentMethod = paymentMethod; }
}