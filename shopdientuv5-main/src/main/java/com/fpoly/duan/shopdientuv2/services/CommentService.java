// src/main/java/com/fpoly/duan/shopdientuv2/services/CommentService.java
package com.fpoly.duan.shopdientuv2.services;

import com.fpoly.duan.shopdientuv2.dto.AttributeDTO;
import com.fpoly.duan.shopdientuv2.dto.CommentRequest;
import com.fpoly.duan.shopdientuv2.dto.CommentResponse;
import com.fpoly.duan.shopdientuv2.entitys.Account;
import com.fpoly.duan.shopdientuv2.entitys.Comment;
import com.fpoly.duan.shopdientuv2.entitys.CommentImages;
import com.fpoly.duan.shopdientuv2.entitys.OrderDetails;
import com.fpoly.duan.shopdientuv2.entitys.ProductAttributeValue;
import com.fpoly.duan.shopdientuv2.jpa.AccountRepository;
import com.fpoly.duan.shopdientuv2.jpa.CommentImagesJPA;
import com.fpoly.duan.shopdientuv2.jpa.CommentJPA;
import com.fpoly.duan.shopdientuv2.jpa.OrderDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private CommentJPA commentRepository;

    @Autowired
    private CommentImagesJPA commentImagesRepository;

    public void addComment(String username, CommentRequest request) {
        if (username == null || username.trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bạn cần đăng nhập để bình luận");
        }

        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Tài khoản không tồn tại"));

        OrderDetails orderDetails = orderDetailRepository
                .findTopByOrderAccountAndProductAttribute_IdOrderByOrder_OrderDateDesc(account,
                        request.getProductAttributeId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.FORBIDDEN,
                        "Bạn chưa có đơn hàng chứa sản phẩm này để được bình luận"));

        Comment comment = new Comment();
        comment.setAccount(account);
        comment.setOrderDetails(orderDetails);
        comment.setRating(request.getRating());
        comment.setDescription(request.getComment());
        comment.setParentId(account.getAccountId());

        Comment savedComment = commentRepository.save(comment);

        List<String> imageUrls = request.getImageUrls();
        if (imageUrls != null && !imageUrls.isEmpty()) {
            for (String imageUrl : imageUrls) {
                // Lưu thông tin hình ảnh bình luận
                com.fpoly.duan.shopdientuv2.entitys.CommentImages commentImage = new com.fpoly.duan.shopdientuv2.entitys.CommentImages();
                commentImage.setImageURL(imageUrl);
                commentImage.setComment(savedComment);
                commentImagesRepository.save(commentImage);
            }
        }
    }

    public List<CommentResponse> getComments(Integer productAttributeId) {
        List<Comment> comments = commentRepository.findByProductAttributeId(productAttributeId);
        List<CommentResponse> responses = new ArrayList<>();

        for (Comment comment : comments) {
            CommentResponse response = new CommentResponse();
            response.setCommentId(comment.getCommentId());
            response.setRating(comment.getRating());
            response.setDescription(comment.getDescription());
            response.setUsername(comment.getAccount().getUsername());

            // Kiểm tra nếu orderDetails không null
            if (comment.getOrderDetails() != null && comment.getOrderDetails().getProductAttribute() != null) {
                // Lấy thông tin sản phẩm từ productAttribute
                response.setProductName(comment.getOrderDetails().getProductAttribute().getProduct().getName());
                response.setProductAttributeSKU(comment.getOrderDetails().getProductAttribute().getSku());

                // Lấy danh sách thuộc tính của biến thể
                List<AttributeDTO> attributeDTOs = new ArrayList<>();
                if (comment.getOrderDetails().getProductAttribute().getProductAttributeValues() != null) {
                    for (ProductAttributeValue pav : comment.getOrderDetails().getProductAttribute()
                            .getProductAttributeValues()) {
                        AttributeDTO attrDTO = new AttributeDTO();
                        attrDTO.setAttributeId(pav.getAttribute().getAttributeId());
                        attrDTO.setAttributeName(pav.getAttribute().getAttributeName());
                        // Chuyển đổi AttributeValue sang String bằng cách lấy tên (getName)
                        attrDTO.setAttributeValue(pav.getValue().getName());
                        attributeDTOs.add(attrDTO);
                    }
                }
                response.setAttributes(attributeDTOs);
            } else {
                // Nếu orderDetails hoặc productAttribute bị null, có thể gán giá trị mặc định
                // hoặc bỏ qua
                response.setProductName("N/A");
                response.setProductAttributeSKU("N/A");
                response.setAttributes(new ArrayList<>());
            }

            // Xử lý ảnh bình luận (nếu có)
            if (comment.getCommentImages() != null && !comment.getCommentImages().isEmpty()) {
                List<String> urls = comment.getCommentImages().stream()
                        .map(CommentImages::getImageURL)
                        .collect(Collectors.toList());
                response.setImageUrls(urls);
            } else {
                response.setImageUrls(new ArrayList<>());
            }
            responses.add(response);
        }
        return responses;
    }

}