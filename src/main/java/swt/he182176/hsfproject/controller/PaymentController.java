package swt.he182176.hsfproject.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import swt.he182176.hsfproject.entity.Enrollment;
import swt.he182176.hsfproject.repository.EnrollmentRepository;
import swt.he182176.hsfproject.service.PaymentService;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    // Sửa lại cho chắc chắn
    @GetMapping("/payos/{id}")
    public String goToPayOS(@PathVariable("id") Integer id) throws Exception {
        System.out.println("DEBUG: Da vao den day voi ID = " + id);
        return "redirect:" + paymentService.createPayOSPayment(id);
    }

    @GetMapping("/vnpay/{id}")
    public String goToVnPay(@PathVariable Integer id, HttpServletRequest request) throws Exception {
        return "redirect:" + paymentService.createVnPayPayment(id, request);
    }

    @GetMapping("/success")
    public String handleSuccess(@RequestParam(value = "orderCode", required = false) Integer enrollmentId,
                                @RequestParam(value = "vnp_ResponseCode", required = false) String vnpCode,
                                @RequestParam(value = "vnp_TxnRef", required = false) Integer vnpEnrollId) {

        // PayOS trả về orderCode, VnPay trả về vnp_TxnRef (tùy cách bạn cấu hình)
        Integer idToUpdate = (enrollmentId != null) ? enrollmentId : vnpEnrollId;

        if (idToUpdate != null) {
            Enrollment en = enrollmentRepository.findById(idToUpdate).orElse(null);
            if (en != null) {
                // Cập nhật trạng thái thành APPROVED
                en.setStatus("APPROVED");
                en.setUpdatedAt(LocalDateTime.now());
                enrollmentRepository.save(en);
                return "redirect:/public-courses?msg=enroll_success";
            }
        }
        return "redirect:/public-courses?msg=payment_error";
    }

    @GetMapping("/cancel")
    public String handleCancel() {
        return "redirect:/public-courses?msg=payment_cancelled";
    }
}