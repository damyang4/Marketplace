//package com.fmi.springcourse.marketplace.comment; // Увери се, че пакетът отговаря на твоята структура
//
//import com.fmi.springcourse.marketplace.comment.entity.Comment;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.UUID;
//
//@Repository
//public interface CommentRepository extends JpaRepository<Comment, Integer> {
//    List<Comment> findAllByProductId(Integer productId);
//    List<Comment> findAllByUserId(UUID userId);
//}