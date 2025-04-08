package com.fpoly.duan.shopdientuv2.services;

import com.fpoly.duan.shopdientuv2.config.Config;
import com.fpoly.duan.shopdientuv2.dto.OrderRequest;
import com.fpoly.duan.shopdientuv2.entitys.*;
import com.fpoly.duan.shopdientuv2.jpa.OrderDetailRepository;
import com.fpoly.duan.shopdientuv2.jpa.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class OrderServiceUser {

    private static final BigDecimal DEFAULT_WEIGHT = BigDecimal.valueOf(1000); // 1kg
    private static final BigDecimal MAX_WEIGHT = BigDecimal.valueOf(5000); // 5kg

    @Autowired
    private CartService cartService;

    @Autowired
    private AddressService addressService;

    @Autowired
    private CouponService couponService;

    @Autowired
    private ShippingService shippingService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailsRepository;

    @Autowired
    private ProductService productService;

    // Hàm tạo URL thanh toán VNPAY dựa trên đơn hàng
    private String generateVnPayUrl(Order order) {
        Map<String, String> vnpParams = new HashMap<>();
        vnpParams.put("vnp_Version", "2.1.0");
        vnpParams.put("vnp_Command", "pay");
        vnpParams.put("vnp_TmnCode", Config.vnp_TmnCode);
        long amount = Math.round(order.getTotalAmount() * 100);
        vnpParams.put("vnp_Amount", String.valueOf(amount));
        vnpParams.put("vnp_CurrCode", "VND");
        vnpParams.put("vnp_TxnRef", order.getOrderId().toString());
        vnpParams.put("vnp_OrderInfo", "Thanh toán đơn hàng: " + order.getOrderId());
        vnpParams.put("vnp_OrderType", "other");
        vnpParams.put("vnp_Locale", "vn");
        vnpParams.put("vnp_ReturnUrl", Config.vnp_ReturnUrl);
        vnpParams.put("vnp_IpAddr", "127.0.0.1");

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnpParams.put("vnp_CreateDate", vnp_CreateDate);
        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnpParams.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnpParams.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        try {
            for (int i = 0; i < fieldNames.size(); i++) {
                String fieldName = fieldNames.get(i);
                String fieldValue = vnpParams.get(fieldName);
                if (fieldValue != null && !fieldValue.isEmpty()) {
                    hashData.append(fieldName).append('=')
                            .append(URLEncoder.encode(fieldValue, "UTF-8"));
                    query.append(URLEncoder.encode(fieldName, "UTF-8"))
                            .append('=')
                            .append(URLEncoder.encode(fieldValue, "UTF-8"));
                    if (i < fieldNames.size() - 1) {
                        hashData.append('&');
                        query.append('&');
                    }
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String secureHash = Config.hmacSHA512(Config.secretKey, hashData.toString());
        query.append("&vnp_SecureHash=").append(secureHash);
        return Config.vnp_PayUrl + "?" + query.toString();
    }

    // Đặt hàng: tạo đơn hàng với trạng thái pending nếu thanh toán VNPAY
    @Transactional(rollbackFor = Exception.class)
    public Order placeOrder(OrderRequest orderRequest, String username) {
        // Lấy địa chỉ giao hàng
        Address address = addressService.getAddressById(orderRequest.getAddressId());
        if (address == null) {
            throw new RuntimeException("Địa chỉ không hợp lệ");
        }
        // Lấy giỏ hàng của người dùng
        List<CartDetails> cartItems = cartService.getCartByUsername(username);
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống");
        }
        // Tính tổng tiền sản phẩm
        BigDecimal totalProducts = cartItems.stream()
                .map(item -> BigDecimal.valueOf(item.getProductAttribute().getPrice())
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        // Tính tổng trọng lượng
        BigDecimal totalWeight = cartItems.stream()
                .map(item -> DEFAULT_WEIGHT.multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        if (totalWeight.compareTo(MAX_WEIGHT) > 0) {
            totalWeight = MAX_WEIGHT;
        }
        // Tính phí vận chuyển
        Integer shippingFeeInt = shippingService.calculateShippingFee(address, totalWeight.intValue());
        BigDecimal shippingFee = BigDecimal.valueOf(shippingFeeInt);

        // Xử lý mã giảm giá
        BigDecimal discount = BigDecimal.ZERO;
        Coupon coupon = null;
        if (orderRequest.getCouponId() != null) {
            coupon = couponService.getCouponById(orderRequest.getCouponId())
                    .orElseThrow(() -> new RuntimeException("Mã giảm giá không tồn tại"));
            if (totalProducts.doubleValue() >= coupon.getMinOrderValue()) {
                discount = totalProducts.multiply(BigDecimal.valueOf(coupon.getDiscountPercentage()))
                        .divide(BigDecimal.valueOf(100));
                if (discount.doubleValue() > coupon.getMaxDiscount()) {
                    discount = BigDecimal.valueOf(coupon.getMaxDiscount());
                }
            }
        }

        // Tổng đơn hàng = sản phẩm + vận chuyển - giảm giá
        BigDecimal totalOrder = totalProducts.add(shippingFee).subtract(discount);

        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(totalOrder.doubleValue());
        order.setAccount(address.getAccount());
        order.setAddress(address);
        order.setCoupon(coupon);
        order.setPaymentMethod(orderRequest.getPaymentMethod());
        // Nếu VNPAY: trạng thái pending, nếu COD: ngay thành công (status = 1)
        if ("VNPAY".equalsIgnoreCase(orderRequest.getPaymentMethod())) {
            order.setStatus(0); // pending
        } else {
            order.setStatus(1); // đã thanh toán
        }

        Order savedOrder = orderRepository.save(order);

        // Lưu order details
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        for (CartDetails cartItem : cartItems) {
            OrderDetails orderDetail = new OrderDetails();
            orderDetail.setOrder(savedOrder);
            orderDetail.setProductAttribute(cartItem.getProductAttribute());
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setPrice(cartItem.getProductAttribute().getPrice());
            orderDetailsList.add(orderDetail);
            // Với COD: giảm tồn kho ngay
            if (!"VNPAY".equalsIgnoreCase(orderRequest.getPaymentMethod())) {
                productService.reduceStock(cartItem.getProductAttribute().getId(), cartItem.getQuantity());
            }
        }
        savedOrder.setOrderDetails(orderDetailsList);
        orderDetailsRepository.saveAll(orderDetailsList);

        // Nếu COD: xử lý giảm số lượng coupon và xóa giỏ hàng
        if (!"VNPAY".equalsIgnoreCase(orderRequest.getPaymentMethod())) {
            if (coupon != null) {
                couponService.reduceCouponQuantity(coupon.getCouponId(), 1);
            }
            cartService.clearCart(username);
        } else {
            // Với VNPAY: trả về URL thanh toán cho FE
            String paymentUrl = generateVnPayUrl(savedOrder);
            savedOrder.setPaymentMethod(paymentUrl);
        }
        return savedOrder;
    }

    // Xác nhận đơn hàng sau thanh toán VNPAY thành công
    @Transactional(rollbackFor = Exception.class)
    public void confirmOrderAfterPayment(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));
        // Cập nhật trạng thái thành đã thanh toán
        order.setStatus(1);
        orderRepository.save(order);
        // Xử lý giảm tồn kho, giảm số lượng coupon, xóa giỏ hàng...
        List<OrderDetails> orderDetails = order.getOrderDetails();
        for (OrderDetails detail : orderDetails) {
            productService.reduceStock(detail.getProductAttribute().getId(), detail.getQuantity());
        }
        if (order.getCoupon() != null) {
            couponService.reduceCouponQuantity(order.getCoupon().getCouponId(), 1);
        }
        // Xóa giỏ hàng của người dùng
        cartService.clearCart(order.getAccount().getUsername());
    }

    // Hủy đơn hàng nếu thanh toán thất bại
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrderAfterPayment(Integer orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại"));
        // Có thể cập nhật trạng thái hủy (ví dụ: status = -1) hoặc xóa đơn hàng
        order.setStatus(-1);
        orderRepository.save(order);
    }

    public List<Order> getOrdersByUsername(String username) {
        return orderRepository.findByAccountUsername(username);
    }
}
