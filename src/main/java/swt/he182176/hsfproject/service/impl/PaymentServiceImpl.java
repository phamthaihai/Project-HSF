package swt.he182176.hsfproject.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import swt.he182176.hsfproject.config.PayOSConfig;
import swt.he182176.hsfproject.entity.Enrollment;
import swt.he182176.hsfproject.service.PaymentService;

import java.util.*;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PayOSConfig config;

    RestTemplate restTemplate = new RestTemplate();

    @Override
    public Map<String, String> createPayment(Enrollment enrollment) {

        String url = "https://api-merchant.payos.vn/v2/payment-requests";

        Map<String, Object> body = new HashMap<>();

        body.put("orderCode", enrollment.getEnrollmentId());
        body.put("amount", enrollment.getCourse().getPrice());
        body.put("description", "Enroll course");
        body.put("returnUrl", "http://localhost:8080/payment/success/" + enrollment.getEnrollmentId());
        body.put("cancelUrl", "http://localhost:8080/payment/cancel");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-client-id", config.getClientId());
        headers.set("x-api-key", config.getApiKey());

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(url, request, Map.class);

        Map data = (Map) response.getBody().get("data");

        Map<String, String> result = new HashMap<>();

        result.put("checkoutUrl", (String) data.get("checkoutUrl"));
        result.put("qrCode", (String) data.get("qrCode"));

        return result;
    }
}