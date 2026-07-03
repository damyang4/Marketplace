//package com.fmi.springcourse.marketplace.rating;
//
//import com.fmi.springcourse.marketplace.exception.UserNotFoundException;
//import com.fmi.springcourse.marketplace.product.ProductRepository;
//import com.fmi.springcourse.marketplace.rating.dto.RatingRequestDTO;
//import com.fmi.springcourse.marketplace.rating.dto.RatingResponseDTO;
//import com.fmi.springcourse.marketplace.rating.entity.Rating;
//import com.fmi.springcourse.marketplace.user.UserRepository;
//import com.fmi.springcourse.marketplace.user.entity.User;
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
//public class RatingService {
//
//    private final RatingRepository ratingRepository;
//    private final UserRepository userRepository;
//    private final ProductRepository productRepository;
//
//    private RatingResponseDTO mapToResponseDTO(Rating rating) {
//        return new RatingResponseDTO(
//                rating.getId(),
//                rating.getNumberOfStars(),
//                rating.getUser(),
//                rating.getProduct()
//        );
//    }
//
//    public List<RatingResponseDTO> getProductRatings(Integer productId) {
//        return ratingRepository.findAllByProductId(productId).stream()
//                .map(this::mapToResponseDTO)
//                .collect(Collectors.toList());
//    }
//
//    @Transactional
//    public RatingResponseDTO addRating(String userEmail, Integer productId, RatingRequestDTO request) {
//
//        User user = userRepository.findByEmail(userEmail)
//                .orElseThrow(() -> new UserNotFoundException("User with email: " + userEmail + " was not found"));
//
//        // finds the products that will receive a rating
//        // fixme: right now productRepository returns a list of products, change it in the future
//        // fixme: add exceptions for not finding a product
//        // List<Product> product = productRepository.id(productId);
//
//        // creates and saves the rating
//        // fixme: problems with Lombok getters, change to proper id's in the future
//        Rating rating = new Rating(UUID.randomUUID(), UUID.randomUUID(), request.numberOfStars());
//        Rating savedRating = ratingRepository.save(rating);
//
//        return mapToResponseDTO(savedRating);
//    }
//
//    @Transactional
//    public RatingResponseDTO updateRating(String userEmail, Integer ratingId, RatingRequestDTO request) {
//        Rating rating = ratingRepository.findById(ratingId)
//                .orElseThrow(() -> new RuntimeException("Rating not found"));
//
//        Optional<User> currUser = Optional.ofNullable(userRepository.findByEmail(userEmail).orElseThrow(
//                () -> new RuntimeException("User cannot be found")));
//
//        if (request.numberOfStars() != null) {
//            rating.setNumberOfStars(request.numberOfStars());
//        }
//
//        return mapToResponseDTO(rating);
//    }
//
//    @Transactional
//    public void deleteRating(String userEmail, Integer ratingId) {
//        Rating rating = ratingRepository.findById(ratingId)
//                .orElseThrow(() -> new RuntimeException("Rating not found"));
//
//        Optional<User> currUser = Optional.ofNullable(userRepository.findByEmail(userEmail).orElseThrow(
//                () -> new RuntimeException("User cannot be found")));
//
//        //fixme: due to problems with Lombok add check is the user the owner of the rating
//
//        ratingRepository.delete(rating);
//    }
//}