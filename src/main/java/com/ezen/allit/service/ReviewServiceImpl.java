package com.ezen.allit.service;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import com.ezen.allit.domain.Hit;
import com.ezen.allit.domain.Product;
import com.ezen.allit.domain.Review;
import com.ezen.allit.domain.Seller;
import com.ezen.allit.dto.HitSaveRequestDto;
import com.ezen.allit.dto.ReviewDeleteRequestDto;
import com.ezen.allit.dto.ReviewModifyRequestDto;
import com.ezen.allit.dto.ReviewSaveRequestDto;
import com.ezen.allit.repository.HitRepository;
import com.ezen.allit.repository.ProductRepository;
import com.ezen.allit.repository.ReviewRepository;
import com.ezen.allit.repository.SellerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	private final ReviewRepository reviewRepo;
	private final ProductRepository productRepo;
	private final SellerRepository sellerRepo;
	private final HitRepository hitRepo;

	/*
	 * 리뷰작성
	 */
//	@Transactional
//	public void saveReview(ReviewSaveRequestDto reviewSaveRequestDto) {
//		reviewRepo.saveReview(reviewSaveRequestDto.getContent(), reviewSaveRequestDto.getImageName(), reviewSaveRequestDto.getRating(), reviewSaveRequestDto.getPno(), reviewSaveRequestDto.getSid());
//	}
	@Transactional
	public void saveReview(ReviewSaveRequestDto reviewSaveRequestDto) {
		reviewRepo.saveReview(reviewSaveRequestDto.getContent(), reviewSaveRequestDto.getImageName(), reviewSaveRequestDto.getRating(), reviewSaveRequestDto.getPno());
	}
	
	/*
	 * 리뷰수정
	 */
	@Transactional
	public void modifyReview(ReviewModifyRequestDto reviewModifyRequestDto) {
		Review review = reviewRepo.findById(reviewModifyRequestDto.getRvno()).get();
		review.setContent(reviewModifyRequestDto.getContent());
		review.setRating(reviewModifyRequestDto.getRating());
	}
	
	/*
	 * 리뷰삭제
	 */
	@Transactional
	public void deleteReview(ReviewDeleteRequestDto reviewDeleteRequestDto) {
		reviewRepo.deleteById(reviewDeleteRequestDto.getRvno());
	}
	
	/*
	 * 리뷰 좋아요
	 */
	@Transactional
	public void hitReview(HitSaveRequestDto hitSaveRequestDto) {
		Optional<Hit> hit = hitRepo.findByReviewRvnoAndSellerId(hitSaveRequestDto.getRvno(), hitSaveRequestDto.getSid());
		Review review = reviewRepo.findById(hitSaveRequestDto.getRvno()).get();
		Seller seller = sellerRepo.findById(hitSaveRequestDto.getSid()).get();
		
		/* 이전에 좋아요 누른 기록이 없으면 좋아요, 있으면 좋아요 취소 */
		if(hit.isEmpty()) {
			hitRepo.save(new Hit(review, seller));
			review.setHit(review.getHit()+1);
		} else {
			hitRepo.deleteById(hit.get().getHno());
			review.setHit(review.getHit()-1);
		}
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
