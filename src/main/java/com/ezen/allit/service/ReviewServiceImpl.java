package com.ezen.allit.service;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ezen.allit.domain.Product;
import com.ezen.allit.dto.ReviewSaveRequestDto;
import com.ezen.allit.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	private final ReviewRepository reviewRepo;

	/*
	 * 리뷰작성
	 */
	@Transactional
	public void saveReview(ReviewSaveRequestDto reviewSaveRequestDto) {
		reviewRepo.saveReview(reviewSaveRequestDto.getContent(), reviewSaveRequestDto.getImageName(), reviewSaveRequestDto.getRating(), reviewSaveRequestDto.getPno());
	}
	
//	/*
//	 * 리뷰작성
//	 */
//	@Transactional
//	public void saveReview(ReviewSaveRequestDto reviewSaveRequestDto, MultipartFile reviewImageFile) throws Exception {
//		System.out.println("reviewImageFile = " + reviewImageFile);
//		String ogName = reviewImageFile.getOriginalFilename(); // 원본 파일명
//		String realPath = "c:/fileUpload/images/"; 		       // 파일 저장경로
//		
//		/*
//		 * UUID를 이용해 중복되지 않는 파일명 생성
//		 */
//		UUID uuid = UUID.randomUUID();
//		String imgName = uuid + "_" + ogName; 			 	   // 저장될 파일명
//		
//		File saveFile = new File(realPath, imgName);     	   // 저장경로와 파일명을 토대로 새 파일 생성 
//		reviewImageFile.transferTo(saveFile);				   // 생성 완료
//		
//		reviewRepo.saveReview(reviewSaveRequestDto.getContent(), imgName, reviewSaveRequestDto.getRating(), reviewSaveRequestDto.getPno());
//	}
}
