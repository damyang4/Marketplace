//package com.fmi.springcourse.marketplace.comment;
//
//import com.fmi.springcourse.marketplace.comment.dto.CommentRequestDTO;
//import com.fmi.springcourse.marketplace.comment.dto.CommentResponseDTO;
//import com.fmi.springcourse.marketplace.user.entity.User;
//import jakarta.validation.Valid;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PatchMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseStatus;
//import org.springframework.web.bind.annotation.RestController;
//
//import java.util.List;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/comments")
//public class CommentController {
//
//    private final CommentService commentService;
//
//    @GetMapping("/product/{productId}")
//    public ResponseEntity<List<CommentResponseDTO>> getProductComments(@PathVariable Integer productId) {
//        return ResponseEntity.ok(commentService.getProductComments(productId));
//    }
//
//    @PostMapping("/product/{productId}")
//    @ResponseStatus(HttpStatus.CREATED)
//    public CommentResponseDTO addComment(@AuthenticationPrincipal User user, @PathVariable Integer productId,
//            @Valid @RequestBody CommentRequestDTO request) {
//        return commentService.addComment(user.getEmail(), productId, request);
//    }
//
//    @PatchMapping("/{commentId}")
//    public CommentResponseDTO updateComment(
//            @AuthenticationPrincipal User user,
//            @PathVariable Integer commentId,
//            @Valid @RequestBody CommentRequestDTO request) {
//
//        return commentService.updateComment(user.getEmail(), commentId, request);
//    }
//
//    @DeleteMapping("/{commentId}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteComment(
//            @AuthenticationPrincipal User user,
//            @PathVariable Integer commentId) {
//
//        commentService.deleteComment(user.getEmail(), commentId);
//    }
//}