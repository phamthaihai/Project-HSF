package swt.he182176.hsfproject.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import swt.he182176.hsfproject.entity.Enrollment;
import swt.he182176.hsfproject.repository.EnrollmentRepository;
import swt.he182176.hsfproject.service.PaymentService;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${payos.clientId}")
    private String payosClientId;

    @Value("${payos.apiKey}")
    private String payosApiKey;

    @Override
    public String createPayOSPayment(Integer enrollmentId) throws Exception {

        Enrollment en = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        int amount = (int) Math.round(en.getCourse().getPrice());

        Map<String, Object> body = new HashMap<>();
        body.put("orderCode", en.getEnrollmentId());
        body.put("amount", amount);
        body.put("description", "Hoc phi " + en.getCourse().getTitle());
        body.put("returnUrl", "http://localhost:8080/HSFProject/payment/success");
        body.put("cancelUrl", "http://localhost:8080/HSFProject/payment/cancel");

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-client-id", payosClientId);
        headers.set("x-api-key", payosApiKey);

        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(body), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
                "https://api-merchant.payos.vn/v2/payment-requests",
                entity,
                String.class
        );

        System.out.println("PayOS response: " + response.getBody());

        JsonNode root = objectMapper.readTree(response.getBody());

        String checkoutUrl = root.path("data").path("checkoutUrl").asText();

        if (checkoutUrl == null || checkoutUrl.isEmpty()) {
            throw new RuntimeException("Không lấy được checkoutUrl từ PayOS");
        }

        return checkoutUrl;
    }

    @Override
    public String createVnPayPayment(Integer enrollmentId, HttpServletRequest request) throws Exception {
        Enrollment en = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        // VnPay thường tính theo đơn vị VNĐ * 100
        long amount = (long) (en.getCourse().getPrice() * 100);

        // Giả sử đây là link redirect của bạn
        return "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?vnp_Amount=" + amount;
    }
}