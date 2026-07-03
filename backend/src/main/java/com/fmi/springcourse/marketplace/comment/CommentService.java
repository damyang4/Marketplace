//package com.fmi.springcourse.marketplace.comment; // Нагласи пакета според вашата структура
//
//import com.fmi.springcourse.marketplace.comment.dto.CommentRequestDTO;
//import com.fmi.springcourse.marketplace.comment.dto.CommentResponseDTO;
//import com.fmi.springcourse.marketplace.comment.entity.Comment;
//import com.fmi.springcourse.marketplace.exception.UserNotFoundException;
//import com.fmi.springcourse.marketplace.product.ProductRepository;
//import com.fmi.springcourse.marketplace.product.entity.Product;
//import com.fmi.springcourse.marketplace.user.entity.User;
//import com.fmi.springcourse.marketplace.user.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//public class CommentService {
//
//    private final CommentRepository commentRepository;
//    private final UserRepository userRepository;
//    private final ProductRepository productRepository;
//
//    private CommentResponseDTO mapToResponseDTO(Comment comment) {
//        return new CommentResponseDTO(
//                comment.getId(),
//                comment.getCommentText(),
//                comment.getUserId(),
//                comment.getProductId()
//        );
//    }
//
//    public List<CommentResponseDTO> getProductComments(Integer productId) {
//        return commentRepository.findAllByProductId(productId).stream()
//                .map(this::mapToResponseDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Transactional
//    public CommentResponseDTO addComment(String userEmail, Long productId, CommentRequestDTO request) {
//
//        // finds the user who is writing the comment
//        User user = userRepository.findByEmail(userEmail)
//                .orElseThrow(() -> new UserNotFoundException("User with email: " + userEmail + " was not found"));
//
//        // finds the products that will receive a comment
//        // fixme: right now productRepository returns a list of products, change it in the future
//        // fixme: add exceptions for not finding a product
//        List<Product> product = productRepository.id(productId);
//
//        // creates and saves the comment
//        // fixme: problems with Lombok getters, change to proper id's in the future
//        Comment comment = new Comment(UUID.randomUUID(), UUID.randomUUID(), request.commentText());
//        Comment savedComment = commentRepository.save(comment);
//
//        return mapToResponseDTO(savedComment);
//    }
//
//    @Transactional
//    public CommentResponseDTO updateComment(String userEmail, Integer commentId, CommentRequestDTO request) {
//        Comment comment = commentRepository.findById(commentId)
//                .orElseThrow(() -> new RuntimeException("Comment not found")); // Замени с CommentNotFoundException
//
//        Optional<User> currUser = Optional.ofNullable(userRepository.findByEmail(userEmail).orElseThrow(
//                () -> new RuntimeException("User cannot be found")));
//
//        if (request.commentText() != null && !request.commentText().isBlank()) {
//            comment.setCommentText(request.commentText());
//        }
//
//        return mapToResponseDTO(comment);
//    }
//
//    @Transactional
//    public void deleteComment(String userEmail, Integer commentId) {
//        Comment comment = commentRepository.findById(commentId)
//                .orElseThrow(() -> new RuntimeException("Comment not found"));
//
//        Optional<User> currUser = Optional.ofNullable(userRepository.findByEmail(userEmail).orElseThrow(
//                () -> new RuntimeException("User cannot be found")));
//
//        //fixme: due to problems with Lombok add check is the user the owner of the comment
//
//        commentRepository.delete(comment);
//    }
//}