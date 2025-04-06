package com.fpoly.duan.shopdientuv2.services;

import com.fpoly.duan.shopdientuv2.entitys.Address;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.*;

@Service
public class ShippingService {

    @Autowired
    private RestTemplate restTemplate;

    private final String GHN_FEE_API_URL = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/fee";
    private final String GHN_AVAILABLE_SERVICE_URL = "https://dev-online-gateway.ghn.vn/shiip/public-api/v2/shipping-order/available-services";
    private final String GHN_TOKEN = "1b6b2e2b-122d-11f0-95d0-0a92b8726859";
    private final String GHN_SHOP_ID = "196331";

    // Thông tin cửa hàng
    private final Integer FROM_DISTRICT_ID = 1574; // Mã quận của cửa hàng
    private final String FROM_WARD_CODE = "550304"; // Mã phường của cửa hàng

    private final Integer DEFAULT_LENGTH = 20;
    private final Integer DEFAULT_WIDTH = 20;
    private final Integer DEFAULT_HEIGHT = 50;
    // Nếu không truyền trọng lượng từ FE thì mặc định là 1kg = 1000 gram
    private final Integer DEFAULT_WEIGHT = 100;
    private final Integer DEFAULT_INSURANCE_VALUE = 1000;
    private final Integer COD_FAILED_AMOUNT = 2000;

    // Lấy service_id từ GHN dựa vào thông tin shop_id, from_district và to_district
    // (được lấy từ address)
    private Integer getValidServiceId(Address address) {
        try {
            if (address.getDistrictId() == null) {
                throw new IllegalArgumentException("Địa chỉ không hợp lệ: districtId bị null");
            }
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("token", GHN_TOKEN);

            int toDistrictId = Integer.parseInt(address.getDistrictId());

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("shop_id", Integer.parseInt(GHN_SHOP_ID));
            requestBody.put("from_district", FROM_DISTRICT_ID);
            requestBody.put("to_district", toDistrictId);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    GHN_AVAILABLE_SERVICE_URL,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class);

            Map<String, Object> responseBody = response.getBody();
            if (response.getStatusCode() == HttpStatus.OK && responseBody != null) {
                Object dataObj = responseBody.get("data");
                if (dataObj instanceof List<?> dataList && !dataList.isEmpty()) {
                    // Lấy dịch vụ đầu tiên từ danh sách khả dụng
                    Object firstItem = dataList.get(0);
                    if (firstItem instanceof Map<?, ?> map) {
                        Object serviceIdObj = map.get("service_id");
                        if (serviceIdObj instanceof Number number) {
                            return number.intValue();
                        }
                    }
                }
            }
            throw new RuntimeException("Không tìm thấy dịch vụ vận chuyển phù hợp.");
        } catch (Exception e) {
            System.err.println("Lỗi khi lấy service_id: " + e.getMessage());
            throw new RuntimeException("Lỗi khi lấy service_id: " + e.getMessage(), e);
        }
    }

    // Tính phí vận chuyển sử dụng thông tin từ address và trọng lượng (nếu weight
    // null thì mặc định 1kg)
    public Integer calculateShippingFee(Address address, Integer weight) {
        try {
            if (address.getDistrictId() == null || address.getWardId() == null) {
                throw new IllegalArgumentException("Địa chỉ không hợp lệ (districtId hoặc wardId bị null)");
            }

            if (weight == null) {
                weight = DEFAULT_WEIGHT;
            }

            // Lấy service_id dựa vào thông tin address
            Integer serviceId = getValidServiceId(address);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.set("Token", GHN_TOKEN);
            headers.set("ShopId", GHN_SHOP_ID);

            int toDistrictId = Integer.parseInt(address.getDistrictId());
            String toWardCode = address.getWardId();

            // Log thông tin giao dịch
            System.out.println("=== Thông tin giao dịch vận chuyển ===");
            System.out.println("Dịch vụ vận chuyển (service_id): " + serviceId);
            System.out.println("Địa chỉ nhận: Quận " + toDistrictId + ", Phường " + toWardCode);
            System.out.println("Trọng lượng tính (gram): " + weight);

            Map<String, Object> body = new HashMap<>();
            body.put("from_district_id", FROM_DISTRICT_ID);
            body.put("from_ward_code", FROM_WARD_CODE);
            body.put("service_id", serviceId);
            body.put("service_type_id", null);
            body.put("to_district_id", toDistrictId);
            body.put("to_ward_code", toWardCode);
            body.put("length", DEFAULT_LENGTH);
            body.put("width", DEFAULT_WIDTH);
            body.put("height", DEFAULT_HEIGHT);
            body.put("weight", weight);
            body.put("insurance_value", DEFAULT_INSURANCE_VALUE);
            body.put("cod_failed_amount", COD_FAILED_AMOUNT);
            body.put("coupon", null);

            // Danh sách item mẫu (nếu cần)
            List<Map<String, Object>> items = new ArrayList<>();
            Map<String, Object> item = new HashMap<>();
            item.put("name", "Item");
            item.put("quantity", 1);
            item.put("length", 200);
            item.put("width", 200);
            item.put("height", 200);
            item.put("weight", weight);
            items.add(item);
            body.put("items", items);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(body, headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    GHN_FEE_API_URL,
                    HttpMethod.POST,
                    requestEntity,
                    Map.class);

            Map<String, Object> responseBody = response.getBody();

            if (response.getStatusCode() == HttpStatus.OK && responseBody != null) {
                Map<String, Object> data = (Map<String, Object>) responseBody.get("data");
                if (data != null && data.get("total") != null) {
                    BigDecimal fee = new BigDecimal(data.get("total").toString());
                    System.out.println("Phí vận chuyển trả về: " + fee.intValue() + " VND");
                    System.out.println("====================================");
                    return fee.intValue();
                }
            }
            throw new RuntimeException("Không thể lấy phí vận chuyển từ GHN.");
        } catch (Exception e) {
            System.err.println("Lỗi khi gọi API GHN: " + e.getMessage());
            throw new RuntimeException("Lỗi khi gọi API GHN: " + e.getMessage(), e);
        }
    }
}
