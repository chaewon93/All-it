package com.ezen.allit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ezen.allit.domain.Review;

public interface ReviewRepository extends JpaRepository<Review, Integer> {

	// 리뷰 작성
	@Modifying
	@Query(value = "INSERT INTO review(content, image_name, rating, pno, mid, prvno, hit, reg_date) VALUES(?1, ?2, ?3, ?4, ?5, 0, 0, sysdate)", nativeQuery = true)
	int saveReview(String content, String imageName, int rating, int pno, String mid);
	
	// 리뷰답글 작성
	@Modifying
	@Query(value = "INSERT INTO review(content, pno, prvno, rating, hit, reg_date) VALUES(?1, ?2, ?3, 0, 0, sysdate)", nativeQuery = true)
	int saveReviewReply(String content, int pno, int prvno);
	
//	@Modifying
//	@Query(value = "INSERT INTO review(content, image_name, rating, pno, mid, hit, reg_date) VALUES(?1, ?2, ?3, ?4, ?5, 0, sysdate)", nativeQuery = true)
//	int saveReview(String content, String imageName, int rating, int pno, int sid);
}
