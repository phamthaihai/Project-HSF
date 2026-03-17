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
    @GetMapping("/repay/{id}")
    public String rePayment(@PathVariable("id") Integer enrollmentId, @RequestParam("method") String method) throws Exception {
        // method có thể là "payos" hoặc "vnpay"
        if ("payos".equalsIgnoreCase(method)) {
            String url = paymentService.createPayOSPayment(enrollmentId);
            return "redirect:" + url;
        } else {
            // Gọi hàm VNPAY bạn vừa sửa ở trên
            // Lưu ý: Cần truyền HttpServletRequest vào nếu dùng code VNPAY mới
            return "redirect:/HSFProject/payment/vnpay?enrollmentId=" + enrollmentId;
        }
    }
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
    public String handleSuccess(@RequestParam(value = "orderCode", required = false) Long orderCode,
                                @RequestParam(value = "vnp_ResponseCode", required = false) String vnpCode,
                                @RequestParam(value = "vnp_TxnRef", required = false) String vnpTxnRef) {
        Integer id = null;

        // Lấy ID từ PayOS (bỏ hậu tố timestamp nếu bạn có dùng)
        if (orderCode != null) id = (int) (orderCode / 10000);
            // Lấy ID từ VNPAY
        else if (vnpTxnRef != null) id = Integer.parseInt(vnpTxnRef);

        if (id != null) {
            // Kiểm tra VNPAY thành công (code 00)
            if (vnpCode != null && !"00".equals(vnpCode)) return "redirect:/public-courses?msg=fail";

            Enrollment en = enrollmentRepository.findById(id).orElse(null);
            if (en != null) {
                en.setStatus("APPROVED");
                en.setUpdatedAt(LocalDateTime.now());
                enrollmentRepository.save(en);
                return "redirect:/public-courses?msg=enroll_success";
            }
        }
        return "redirect:/public-courses?msg=error";
    }

    @GetMapping("/cancel")
    public String handleCancel() {
        return "redirect:/public-courses?msg=payment_cancelled";
    }
}