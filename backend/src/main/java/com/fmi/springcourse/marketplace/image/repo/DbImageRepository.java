package com.fmi.springcourse.marketplace.image.repo;

import com.fmi.springcourse.marketplace.image.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DbImageRepository extends JpaRepository<Image, Long> {
	void deleteByNameInBucket(String nameInBucket);
	
	Optional<Image> findByNameInBucket(String nameInBucket);
}
