package swt.he182176.hsfproject.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper; // Cần thiết
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import swt.he182176.hsfproject.entity.Enrollment;
import swt.he182176.hsfproject.repository.EnrollmentRepository;
import swt.he182176.hsfproject.service.PaymentService;

import jakarta.servlet.http.HttpServletRequest;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private EnrollmentRepository enrollmentRepository;


    private final ObjectMapper objectMapper = new ObjectMapper(); // Spring sẽ tự inject cái này vào

    @Value("${payos.clientId}")
    private String payosClientId;

    @Value("${payos.apiKey}")
    private String payosApiKey;

    @Value("${payos.checksumKey}")
    private String payosChecksumKey;

    @Override
    public String createPayOSPayment(Integer enrollmentId) throws Exception {
        Enrollment en = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        // Quy đổi số tiền (ví dụ từ giá khóa học sang VND)
        int amount = (int) Math.round(en.getCourse().getPrice());
        if (amount < 2000) amount = 2000;

        // GIẢI PHÁP: Tạo mã orderCode không trùng lặp
        // PayOS giới hạn orderCode là kiểu Number (tối đa 15-18 chữ số)
        // Ta lấy ID đơn hàng + 4 số cuối của timestamp để tạo mã duy nhất
        long timestampSuffix = System.currentTimeMillis() % 10000;
        long finalOrderCode = Long.parseLong(enrollmentId + "" + String.format("%04d", timestampSuffix));

        Map<String, Object> params = new HashMap<>();
        params.put("orderCode", finalOrderCode); // Dùng mã mới tạo
        params.put("amount", amount);
        params.put("description", "Hoc phi " + enrollmentId);
        params.put("cancelUrl", "http://localhost:8080/HSFProject/payment/cancel");
        params.put("returnUrl", "http://localhost:8080/HSFProject/payment/success");

        String signature = calculateSignature(params, payosChecksumKey);
        params.put("signature", signature);

        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-client-id", payosClientId);
        headers.set("x-api-key", payosApiKey);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(
                    "https://api-merchant.payos.vn/v2/payment-requests",
                    entity,
                    String.class
            );

            JsonNode responseBody = objectMapper.readTree(response.getBody());

            if (responseBody != null && "00".equals(responseBody.get("code").asText())) {
                return responseBody.get("data").get("checkoutUrl").asText();
            } else {
                String errorDesc = (responseBody != null) ? responseBody.get("desc").asText() : "Unknown Error";
                throw new RuntimeException("Lỗi từ PayOS: " + errorDesc);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Không thể kết nối tới PayOS: " + e.getMessage());
        }
    }
    private String calculateSignature(Map<String, Object> data, String checksumKey) throws Exception {
        TreeMap<String, Object> sortedData = new TreeMap<>(data);
        String dataString = sortedData.entrySet().stream()
                .map(e -> e.getKey() + "=" + e.getValue())
                .collect(Collectors.joining("&"));

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(checksumKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        byte[] hash = sha256_HMAC.doFinal(dataString.getBytes(StandardCharsets.UTF_8));

        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    @Override
    public String createVnPayPayment(Integer enrollmentId, HttpServletRequest request) throws Exception {
        Enrollment en = enrollmentRepository.findById(enrollmentId)
                .orElseThrow(() -> new RuntimeException("Enrollment not found"));

        String vnp_TmnCode = "358SLJ3B";
        String vnp_HashSecret = "YLJM4HA5QOVZKDHER251NG024ME8UHON";
        String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";

        Map<String, String> vnp_Params = new TreeMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", "pay");
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf((long) (en.getCourse().getPrice() * 100)));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", String.valueOf(en.getEnrollmentId()));
        vnp_Params.put("vnp_OrderInfo", "Thanh toan hoc phi " + enrollmentId);
        vnp_Params.put("vnp_OrderType", "other");
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", "http://localhost:8080/HSFProject/payment/success");
        vnp_Params.put("vnp_IpAddr", request.getRemoteAddr());
        vnp_Params.put("vnp_CreateDate", new java.text.SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date()));

        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();

        for (Map.Entry<String, String> entry : vnp_Params.entrySet()) {
            String key = entry.getKey();
            String value = entry.getValue();
            if (value != null && !value.isEmpty()) {

                hashData.append(key).append('=').append(value).append('&');

                query.append(java.net.URLEncoder.encode(key, StandardCharsets.US_ASCII.toString()))
                        .append('=')
                        .append(java.net.URLEncoder.encode(value, StandardCharsets.US_ASCII.toString()))
                        .append('&');
            }
        }


        String queryUrl = query.substring(0, query.length() - 1);
        String dataToHash = hashData.substring(0, hashData.length() - 1);

        queryUrl = queryUrl.replace("+", "%20");

        String vnp_SecureHash = hmacSHA512(vnp_HashSecret, dataToHash);

        return vnp_Url + "?" + queryUrl + "&vnp_SecureHash=" + vnp_SecureHash;
    }

    private String hmacSHA512(String key, String data) throws Exception {
        Mac hmacSha512 = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
        hmacSha512.init(secretKey);
        byte[] hash = hmacSha512.doFinal(data.getBytes(StandardCharsets.UTF_8));
        StringBuilder sb = new StringBuilder();
        for (byte b : hash) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}