package com.ezen.allit.service;

import com.ezen.allit.dto.HitSaveRequestDto;
import com.ezen.allit.dto.ReviewDeleteRequestDto;
import com.ezen.allit.dto.ReviewDto;
import com.ezen.allit.dto.ReviewModifyRequestDto;
import com.ezen.allit.dto.ReviewReplySaveRequestDto;
import com.ezen.allit.dto.ReviewSaveRequestDto;

public interface ReviewService {
	// 리뷰작성
	void saveReview(ReviewSaveRequestDto reviewSaveRequestDto);

	// 리뷰수정
	void modifyReview(ReviewModifyRequestDto reviewModifyRequestDto);
	
	// 리뷰삭제	
	void deleteReview1(ReviewDeleteRequestDto reviewDeleteRequestDto);
	void deleteReview2(ReviewDto reviewDto);
	
	// 리뷰 좋아요
	void hitReview(HitSaveRequestDto hitSaveRequestDto);
	
	// 리뷰답글작성
	void saveReviewReply(ReviewReplySaveRequestDto reviewReplySaveRequestDto);
}
