// src/main/java/com/fpoly/duan/shopdientuv2/controllers/CommentController.java
package com.fpoly.duan.shopdientuv2.controllers;

import com.fpoly.duan.shopdientuv2.dto.CommentRequest;
import com.fpoly.duan.shopdientuv2.dto.CommentResponse;
import com.fpoly.duan.shopdientuv2.services.CommentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addComment(@RequestHeader("X-Username") String username,
            @RequestBody CommentRequest request) {
        commentService.addComment(username, request);
    }

    @GetMapping
    public List<CommentResponse> getComments(@RequestParam("productAttributeId") Integer productAttributeId) {
        return commentService.getComments(productAttributeId);
    }
}
