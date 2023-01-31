package com.ezen.allit.service;

import org.springframework.web.multipart.MultipartFile;

import com.ezen.allit.dto.ReviewSaveRequestDto;

public interface ReviewService {
	void saveReview(ReviewSaveRequestDto reviewSaveRequestDto);
	
	void deleteReview(int rvno);
}
