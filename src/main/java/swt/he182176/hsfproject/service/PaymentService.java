package swt.he182176.hsfproject.service;

import jakarta.servlet.http.HttpServletRequest;

public interface PaymentService {
    // Nhận vào Integer (ID) thay vì cả đối tượng Enrollment
    String createPayOSPayment(Integer enrollmentId) throws Exception;
    String createVnPayPayment(Integer enrollmentId, HttpServletRequest request) throws Exception;
}