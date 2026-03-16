package swt.he182176.hsfproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import swt.he182176.hsfproject.config.PayOSConfig;
import swt.he182176.hsfproject.entity.Enrollment;
import swt.he182176.hsfproject.service.PayOSService;

@Service
public class PayOSServiceImpl implements PayOSService {

    @Autowired
    private PayOSConfig payOSConfig;

    @Override
    public String createPaymentLink(Enrollment enrollment) {

        int amount = (int) enrollment.getCourse().getPrice();

        String description = "Enroll course " + enrollment.getCourse().getTitle();

        String returnUrl = "http://localhost:8080/payment/success/" + enrollment.getEnrollmentId();

        String cancelUrl = "http://localhost:8080/payment/cancel";

        // Demo QR PayOS
        String paymentUrl =
                "https://pay.payos.vn/web/" + enrollment.getEnrollmentId();

        return paymentUrl;
    }
}