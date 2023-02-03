package com.ezen.allit.service;

import org.springframework.web.multipart.MultipartFile;

import com.ezen.allit.dto.HitSaveRequestDto;
import com.ezen.allit.dto.ReviewDeleteRequestDto;
import com.ezen.allit.dto.ReviewSaveRequestDto;

public interface ReviewService {
	// 리뷰작성
	void saveReview(ReviewSaveRequestDto reviewSaveRequestDto);

	// 리뷰수정
//	void modifyReview(ReviewSaveRequestDto reviewSaveRequestDto, int rvno);
	
	// 리뷰삭제	
	void deleteReview(int rvno);
	
	// 리뷰 좋아요
	void hitReview(HitSaveRequestDto hitSaveRequestDto);
}
