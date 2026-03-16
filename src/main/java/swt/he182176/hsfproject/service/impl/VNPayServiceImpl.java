package swt.he182176.hsfproject.service.impl;

import org.springframework.stereotype.Service;
import swt.he182176.hsfproject.entity.Enrollment;
import swt.he182176.hsfproject.service.VNPayService;

@Service
public class VNPayServiceImpl implements VNPayService {

    @Override
    public String createPaymentUrl(Enrollment enrollment) {

        int amount = (int) enrollment.getCourse().getPrice() * 100;

        String orderInfo = "Enroll course " + enrollment.getCourse().getTitle();

        return "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";
    }
}