package com.fpoly.duan.shopdientuv2.services;

import com.fpoly.duan.shopdientuv2.entitys.Comment;
import com.fpoly.duan.shopdientuv2.entitys.CommentImages;
import com.fpoly.duan.shopdientuv2.jpa.CommentJPA;
import com.fpoly.duan.shopdientuv2.jpa.ProductJPA;
import com.fpoly.duan.shopdientuv2.jpa.AccountRepository;
import com.fpoly.duan.shopdientuv2.jpa.CommentImagesJPA;

import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ReviewService {

    @Autowired
    private CommentJPA commentRepository;

    @Autowired
    private CommentImagesJPA commentImagesRepository;

    @Autowired
    private ProductJPA productRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Comment submitReview(Integer productId, Integer accountId, Byte rating, String commentText,
            MultipartFile[] images) throws Exception {
        // Kiểm tra thông tin sản phẩm và tài khoản
        if (!productRepository.existsById(productId)) {
            throw new Exception("Sản phẩm không tồn tại.");
        }
        if (!accountRepository.existsById(accountId)) {
            throw new Exception("Tài khoản không tồn tại.");
        }

        // Tạo đối tượng Comment (đánh giá)
        Comment review = new Comment();
        review.setRating(rating);
        review.setDescription(commentText);
        // Bạn có thể set thêm các thông tin khác như parentId nếu cần

        // Liên kết sản phẩm và tài khoản cho review
        // review.setProduct(...); // nếu có quan hệ giữa Comment và Product
        // review.setAccount(...);

        // Lưu review trước để lấy id
        Comment savedReview = commentRepository.save(review);

        // Xử lý upload hình ảnh nếu có
        if (images != null && images.length > 0) {
            List<CommentImages> imageList = new ArrayList<>();
            for (MultipartFile file : images) {
                // Ở đây bạn cần lưu file vào storage (file system, cloud, ...)
                // Sau đó lấy đường dẫn (imageURL) lưu vào database
                // Ví dụ đơn giản:
                String imageUrl = "/uploads/" + file.getOriginalFilename();
                CommentImages commentImage = new CommentImages();
                commentImage.setImageURL(imageUrl);
                commentImage.setComment(savedReview);
                imageList.add(commentImage);
            }
            commentImagesRepository.saveAll(imageList);
        }
        return savedReview;
    }
}
