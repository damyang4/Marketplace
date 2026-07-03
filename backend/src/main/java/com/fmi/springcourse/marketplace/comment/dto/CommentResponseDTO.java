//package com.fmi.springcourse.marketplace.comment.dto;
//
//import com.fmi.springcourse.marketplace.comment.entity.Comment;
//import java.util.UUID;
//
//public record CommentResponseDTO(Integer id, String commentText, UUID userId, UUID productId) {
//
//    public CommentResponseDTO(Comment comment) {
//        this(
//                comment.getId(),
//                comment.getCommentText(),
//                comment.getUserId(),
//                comment.getProductId()
//        );
//    }
//}