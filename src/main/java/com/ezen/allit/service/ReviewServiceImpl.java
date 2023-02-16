package com.ezen.allit.service;

import java.util.Date;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ezen.allit.domain.Hit;
import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.Review;
import com.ezen.allit.dto.HitDto;
import com.ezen.allit.dto.ReviewDeleteRequestDto;
import com.ezen.allit.dto.ReviewDto;
import com.ezen.allit.dto.ReviewModifyRequestDto;
import com.ezen.allit.dto.ReviewReplySaveRequestDto;
import com.ezen.allit.dto.ReviewSaveRequestDto;
import com.ezen.allit.repository.HitRepository;
import com.ezen.allit.repository.MemberRepository;
import com.ezen.allit.repository.ReviewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {
	private final ReviewRepository reviewRepo;
	private final HitRepository hitRepo;
	private final MemberRepository memberRepo;
	
	/*
	 * 리뷰작성
	 */
	@Transactional
	public void saveReview(ReviewSaveRequestDto reviewSaveRequestDto) {
		reviewRepo.saveReview(reviewSaveRequestDto.getContent(), reviewSaveRequestDto.getImageName(), reviewSaveRequestDto.getRating(), reviewSaveRequestDto.getPno(), reviewSaveRequestDto.getMid());
	}
	
	/*
	 * 리뷰수정
	 */
	@Transactional
	public void modifyReview(ReviewModifyRequestDto reviewModifyRequestDto) {
		Review review = reviewRepo.findById(reviewModifyRequestDto.getRvno()).get();
		review.setContent(reviewModifyRequestDto.getContent());
		review.setRating(reviewModifyRequestDto.getRating());
		review.setModDate(new Date());
	}
	
	/*
	 * 리뷰삭제
	 */
	@Transactional
	public void deleteReview1(ReviewDeleteRequestDto reviewDeleteRequestDto) {
		Review review = reviewRepo.findById(reviewDeleteRequestDto.getRvno()).get();
		review.getOrdersDetail().setStatus(4);
		reviewRepo.deleteById(reviewDeleteRequestDto.getRvno());
	}
	
	/*
	 * 리뷰 좋아요
	 */
	@Transactional
	public void hitReview(HitDto hitDto) {
		Optional<Hit> hit = hitRepo.findByReviewRvnoAndMemberId(hitDto.getRvno(), hitDto.getMid());
		Review review = reviewRepo.findById(hitDto.getRvno()).get();
		Member member = memberRepo.findById(hitDto.getMid()).get();
		
		/* 이전에 좋아요 누른 기록이 없으면 좋아요, 있으면 좋아요 취소 */
		if(hit.isEmpty()) {
			hitRepo.save(new Hit(review, member));
			review.setHit(review.getHit()+1);
		} else {
			hitRepo.deleteById(hit.get().getHno());
			review.setHit(review.getHit()-1);
		}
	}
	
	/*
	 * 리뷰답글작성
	 */

	@Transactional
	public void saveReviewReply(ReviewReplySaveRequestDto reviewReplySaveRequestDto) {
		reviewRepo.saveReviewReply(reviewReplySaveRequestDto.getContent(), reviewReplySaveRequestDto.getPno(), reviewReplySaveRequestDto.getRvno());
	}

	
	/*
	 * 리뷰삭제
	 */
	@Transactional
	public void deleteReview2(ReviewDto reviewDto) {
		Review review = reviewRepo.findById(reviewDto.getRvno()).get();
		review.getOrdersDetail().setStatus(4);
		reviewRepo.deleteById(reviewDto.getRvno());
	}

}
