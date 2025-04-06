package com.fpoly.duan.shopdientuv2.services;

import com.fpoly.duan.shopdientuv2.dto.OrderRequest;
import com.fpoly.duan.shopdientuv2.entitys.*;
import com.fpoly.duan.shopdientuv2.jpa.OrderDetailRepository;
import com.fpoly.duan.shopdientuv2.jpa.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * Đặt đơn hàng cho người dùng dựa trên thông tin từ giỏ hàng và địa chỉ nhận
     * hàng.
     */
    public Order placeOrder(OrderRequest orderRequest) {
        // Lấy địa chỉ giao hàng từ orderRequest
        Address address = addressService.getAddressById(orderRequest.getAddressId());
        if (address == null) {
            throw new RuntimeException("Địa chỉ không hợp lệ");
        }

        // Lấy username từ account đã được liên kết với Address
        String username = address.getAccount().getUsername();

        // Lấy tất cả sản phẩm trong giỏ hàng của người dùng
        List<CartDetails> cartItems = cartService.getCartByUsername(username); // Tìm theo username
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống");
        }

        // Tiến hành tính toán các chi phí (tương tự như trước)
        BigDecimal totalProducts = cartItems.stream()
                .map(item -> BigDecimal.valueOf(item.getProductAttribute().getPrice())
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalWeight = cartItems.stream()
                .map(item -> DEFAULT_WEIGHT.multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalWeight.compareTo(MAX_WEIGHT) > 0) {
            totalWeight = MAX_WEIGHT;
        }

        Integer shippingFeeInt = shippingService.calculateShippingFee(address, totalWeight.intValue());
        BigDecimal shippingFee = BigDecimal.valueOf(shippingFeeInt);

        // Tính giảm giá nếu có
        BigDecimal discount = BigDecimal.ZERO;
        Coupon coupon = null;
        if (orderRequest.getCouponId() != null) {
            coupon = couponService.getCouponById(orderRequest.getCouponId())
                    .orElseThrow(() -> new RuntimeException("Mã giảm giá không tồn tại"));
            if (totalProducts.doubleValue() >= coupon.getMinOrderValue()) {
                discount = totalProducts
                        .multiply(BigDecimal.valueOf(coupon.getDiscountPercentage()))
                        .divide(BigDecimal.valueOf(100));
                if (discount.doubleValue() > coupon.getMaxDiscount()) {
                    discount = BigDecimal.valueOf(coupon.getMaxDiscount());
                }
            }
        }

        BigDecimal totalOrder = totalProducts.add(shippingFee).subtract(discount);

        // Tạo và lưu đơn hàng
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setTotalAmount(totalOrder.doubleValue());
        order.setStatus("Đang xử lý");
        order.setAccount(address.getAccount());
        order.setAddress(address);
        order.setCoupon(coupon);

        Order savedOrder = orderRepository.save(order);

        // Lưu các chi tiết đơn hàng
        List<OrderDetails> orderDetailsList = new ArrayList<>();
        for (CartDetails cartItem : cartItems) {
            OrderDetails orderDetail = new OrderDetails();
            orderDetail.setOrder(savedOrder);
            orderDetail.setProductAttribute(cartItem.getProductAttribute());
            orderDetail.setQuantity(cartItem.getQuantity());
            orderDetail.setPrice(cartItem.getProductAttribute().getPrice());
            orderDetailsList.add(orderDetail);

            // Giảm số lượng tồn kho
            productService.reduceStock(cartItem.getProductAttribute().getId(), cartItem.getQuantity());
        }

        savedOrder.setOrderDetails(orderDetailsList);
        orderDetailsRepository.saveAll(orderDetailsList);

        // Cập nhật số lượng mã giảm giá nếu có
        if (coupon != null) {
            couponService.reduceCouponQuantity(coupon.getCouponId(), 1);
        }

        // Xóa giỏ hàng của người dùng sau khi đặt hàng thành công
        cartService.clearCart(username);

        return savedOrder;
    }
}
