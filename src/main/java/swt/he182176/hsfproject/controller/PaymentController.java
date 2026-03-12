package swt.he182176.hsfproject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import swt.he182176.hsfproject.entity.Enrollment;
import swt.he182176.hsfproject.repository.EnrollmentRepository;
import swt.he182176.hsfproject.service.PaymentService;
import swt.he182176.hsfproject.service.VNPayService;

import java.util.Map;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private VNPayService vnpayService;

    @GetMapping("/payos/{id}")
    public String payosPayment(@PathVariable int id, Model model) {

        Enrollment enrollment =
                enrollmentRepository.findById(id).orElseThrow();

        Map<String, String> paymentData =
                paymentService.createPayment(enrollment);

        model.addAttribute("checkoutUrl", paymentData.get("checkoutUrl"));
        model.addAttribute("qrCode", paymentData.get("qrCode"));

        return "payment/payos";
    }

    @GetMapping("/vnpay/{id}")
    public String vnpayPayment(@PathVariable int id) {

        Enrollment enrollment = enrollmentRepository.findById(id).orElseThrow();

        String paymentUrl = vnpayService.createPaymentUrl(enrollment);

        return "redirect:" + paymentUrl;
    }

    @GetMapping("/success/{id}")
    public String paymentSuccess(@PathVariable int id) {

        Enrollment enrollment = enrollmentRepository.findById(id).orElseThrow();

        enrollment.setStatus("APPROVED");

        enrollmentRepository.save(enrollment);

        return "payment/success";
    }
}