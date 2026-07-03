//package com.fmi.springcourse.marketplace.rating;
//
//import com.fmi.springcourse.marketplace.rating.dto.RatingRequestDTO;
//import com.fmi.springcourse.marketplace.rating.dto.RatingResponseDTO;
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
//@RequestMapping("/ratings")
//public class RatingController {
//
//    private final RatingService ratingService;
//
//    @GetMapping("/product/{productId}")
//    public ResponseEntity<List<RatingResponseDTO>> getProductRatings(@PathVariable Integer productId) {
//        return ResponseEntity.ok(ratingService.getProductRatings(productId));
//    }
//
//    @PostMapping("/product/{productId}")
//    @ResponseStatus(HttpStatus.CREATED)
//    public RatingResponseDTO addRating(@AuthenticationPrincipal User user, @PathVariable Integer productId,
//                                       @Valid @RequestBody RatingRequestDTO request) {
//        return ratingService.addRating(user.getEmail(), productId, request);
//    }
//
//    @PatchMapping("/{ratingId}")
//    public RatingResponseDTO updateRating(
//            @AuthenticationPrincipal User user,
//            @PathVariable Integer ratingId,
//            @Valid @RequestBody RatingRequestDTO request) {
//
//        return ratingService.updateRating(user.getEmail(), ratingId, request);
//    }
//
//    @DeleteMapping("/{ratingId}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void deleteRating(
//            @AuthenticationPrincipal User user,
//            @PathVariable Integer ratingId) {
//
//        ratingService.deleteRating(user.getEmail(), ratingId);
//    }
//}