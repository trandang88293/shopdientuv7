package com.fpoly.duan.shopdientuv2.controllers;

import com.fpoly.duan.shopdientuv2.config.Config;
import com.fpoly.duan.shopdientuv2.services.OrderServiceUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;

@RestController
@RequestMapping("/payment_return")
@CrossOrigin(origins = "http://localhost:3000")
public class PaymentReturnController {

    @Autowired
    private OrderServiceUser orderServiceUser;

    @GetMapping
    public ResponseEntity<?> handleVnPayReturn(HttpServletRequest request) {
        // Lấy các tham số trả về từ VNPAY
        Map<String, String[]> parameterMap = request.getParameterMap();
        Map<String, String> vnpParams = new HashMap<>();
        parameterMap.forEach((key, value) -> vnpParams.put(key, value[0]));

        // Lấy giá trị cần thiết
        String responseCode = vnpParams.get("vnp_ResponseCode");
        String txnRef = vnpParams.get("vnp_TxnRef");
        String vnpSecureHash = vnpParams.get("vnp_SecureHash");

        // Xóa secure hash để tạo chuỗi hash xác thực
        vnpParams.remove("vnp_SecureHash");
        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        try {
            for (int i = 0; i < fieldNames.size(); i++) {
                String fieldName = fieldNames.get(i);
                String fieldValue = vnpParams.get(fieldName);
                if (fieldValue != null && !fieldValue.isEmpty()) {
                    hashData.append(fieldName).append('=')
                            .append(URLEncoder.encode(fieldValue, "UTF-8"));
                    if (i < fieldNames.size() - 1) {
                        hashData.append('&');
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String calculatedHash = Config.hmacSHA512(Config.secretKey, hashData.toString());

        if (!calculatedHash.equals(vnpSecureHash)) {
            return ResponseEntity.badRequest().body("Invalid signature");
        }

        if ("00".equals(responseCode)) {
            // Chuyển đổi txnRef sang Integer
            orderServiceUser.confirmOrderAfterPayment(Integer.parseInt(txnRef));
            // Trả về thông báo thành công cho FE
            return ResponseEntity.ok("Thanh toán thành công");
        } else {
            orderServiceUser.cancelOrderAfterPayment(Integer.parseInt(txnRef));
            return ResponseEntity.badRequest().body("Thanh toán thất bại");
        }
    }
}
