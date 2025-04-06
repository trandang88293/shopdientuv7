package com.fpoly.duan.shopdientuv2.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fpoly.duan.shopdientuv2.dto.CartAddRequest;
import com.fpoly.duan.shopdientuv2.entitys.Account;
import com.fpoly.duan.shopdientuv2.entitys.Cart;
import com.fpoly.duan.shopdientuv2.entitys.CartDetails;
import com.fpoly.duan.shopdientuv2.entitys.ProductAttribute;
import com.fpoly.duan.shopdientuv2.jpa.AccountRepository;
import com.fpoly.duan.shopdientuv2.jpa.CartDetailsRepository;
import com.fpoly.duan.shopdientuv2.jpa.CartRepository;
import com.fpoly.duan.shopdientuv2.jpa.ProductAttributeJPA;

@Service
public class CartService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartDetailsRepository cartDetailsRepository;

    @Autowired
    private ProductAttributeJPA productAttributeJPA;

    // Thêm sản phẩm vào giỏ
    public CartDetails addToCart(String username, CartAddRequest request) {
        // Lấy tài khoản dựa trên username
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Tìm hoặc tạo giỏ hàng cho tài khoản
        Cart cart = cartRepository.findByAccount(account).orElseGet(() -> {
            Cart newCart = new Cart();
            newCart.setAccount(account);
            return cartRepository.save(newCart);
        });

        // Lấy biến thể sản phẩm theo productAttributeId
        ProductAttribute productAttribute = productAttributeJPA.findById(request.getProductAttributeId())
                .orElseThrow(() -> new RuntimeException("Product attribute not found"));

        // Nếu sản phẩm đã có trong giỏ, cập nhật số lượng
        Optional<CartDetails> existingItem = cartDetailsRepository.findByCartAndProductAttribute(cart,
                productAttribute);
        if (existingItem.isPresent()) {
            CartDetails cd = existingItem.get();
            cd.setQuantity(cd.getQuantity() + request.getQuantity());
            return cartDetailsRepository.save(cd);
        } else {
            CartDetails cartDetails = new CartDetails();
            cartDetails.setCart(cart);
            cartDetails.setProductAttribute(productAttribute);
            cartDetails.setQuantity(request.getQuantity());
            cartDetails.setAttributes(request.getAttributes());
            return cartDetailsRepository.save(cartDetails);
        }
    }

    // Cập nhật số lượng sản phẩm trong giỏ
    public CartDetails updateCartItem(Integer cartDetailsId, int quantity) {
        CartDetails cartDetails = cartDetailsRepository.findById(cartDetailsId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        cartDetails.setQuantity(quantity);
        return cartDetailsRepository.save(cartDetails);
    }

    // Xóa sản phẩm khỏi giỏ
    public void deleteCartItem(Integer cartDetailsId) {
        CartDetails cartDetails = cartDetailsRepository.findById(cartDetailsId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));
        cartDetailsRepository.delete(cartDetails);
    }

    // Lấy giỏ hàng của người dùng theo username
    public List<CartDetails> getCartByUsername(String username) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        Cart cart = cartRepository.findByAccount(account)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
        return cartDetailsRepository.findByCart(cart);
    }

    // Lấy đối tượng Account dựa trên username (dùng trong quá trình đặt hàng)
    public Account getAccountByUsername(String username) {
        return accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Account not found"));
    }

    // Xóa giỏ hàng của người dùng (xóa tất cả các sản phẩm trong giỏ)
    public void clearCart(String username) {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Account not found"));
        Cart cart = cartRepository.findByAccount(account).orElse(null);
        if (cart != null) {
            List<CartDetails> items = cartDetailsRepository.findByCart(cart);
            cartDetailsRepository.deleteAll(items);
        }
    }

}
