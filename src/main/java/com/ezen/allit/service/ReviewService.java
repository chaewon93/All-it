package com.ezen.allit.service;

import com.ezen.allit.dto.HitDto;
import com.ezen.allit.dto.ReviewDto;

public interface ReviewService {
	// 리뷰작성
	void saveReview(ReviewDto reviewDt);

	// 리뷰수정
	void modifyReview(ReviewDto reviewDt);
	
	// 리뷰삭제	
	void deleteReview1(ReviewDto reviewDt);
	void deleteReview2(ReviewDto reviewDto);
	
	// 리뷰 좋아요
	void hitReview(HitDto hitSaveRequestDto);
	
	// 리뷰답글작성
	void saveReviewReply(ReviewDto reviewDt);
}
