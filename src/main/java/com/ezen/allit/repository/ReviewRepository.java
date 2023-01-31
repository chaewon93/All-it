package com.ezen.allit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ezen.allit.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {
	@Modifying
	@Query(value = "INSERT INTO review(content, image_name, rating, pno, hit, reg_date) VALUES(?1, ?2, ?3, ?4, 0, sysdate)", nativeQuery = true)
	int saveReview(String content, String imageName, int rating, int pno);	
	
//	@Modifying
//	@Query(value = "INSERT INTO review(content, image_name, rating, pno, sid, hit, reg_date) VALUES(?1, ?2, ?3, ?4, ?5, 0, sysdate)", nativeQuery = true)
//	int saveReview(String content, String imageName, int rating, int pno, int sid);	
}
