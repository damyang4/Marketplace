//package com.fmi.springcourse.marketplace.rating;
//
//import com.fmi.springcourse.marketplace.rating.entity.Rating;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.UUID;
//
//@Repository
//public interface RatingRepository extends JpaRepository<Rating, Integer> {
//    List<Rating> findAllByProductId(Integer productId);
//
//    List<Rating> findAllByUserId(UUID userId);
//}