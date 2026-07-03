//package com.fmi.springcourse.marketplace.rating.dto;
//
//import com.fmi.springcourse.marketplace.rating.entity.Rating;
//import java.util.UUID;
//
//public record RatingResponseDTO(Integer id, Integer numberOfStars, UUID userId, UUID productId) {
//
//    public RatingResponseDTO(Rating rating) {
//        this(
//                rating.getId(),
//                rating.getNumberOfStars(),
//                rating.getUser(),
//                rating.getProduct()
//        );
//    }
//}