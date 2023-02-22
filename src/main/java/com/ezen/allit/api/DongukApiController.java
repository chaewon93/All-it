package com.ezen.allit.api;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ezen.allit.domain.Member;
import com.ezen.allit.dto.HitDto;
import com.ezen.allit.dto.MemberDto;
import com.ezen.allit.dto.OrdersDetailDto;
import com.ezen.allit.dto.QnADto;
import com.ezen.allit.dto.ResponseDto;
import com.ezen.allit.dto.ReviewDto;
import com.ezen.allit.repository.MemberRepository;
import com.ezen.allit.service.MemberService;
import com.ezen.allit.service.OrderService;
import com.ezen.allit.service.QnAService;
import com.ezen.allit.service.ReviewService;
import com.ezen.allit.service.SellerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class DongukApiController {
	private final SellerService sellerService;
	private final ReviewService reviewService;
	private final MemberService memberService;
	private final QnAService qnaService;
	private final OrderService orderService;
	private final MemberRepository memberRepo;

	/*
	 * 상품삭제
	 */
	@DeleteMapping("/seller/product/delete/{pno}")
	public ResponseDto<Integer> deleteProduct(@PathVariable int pno) {
		sellerService.deleteProduct(pno);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	/*
	 * 상품 좋아요
	 */
	@PutMapping("/product/hit/{pno}")
	public ResponseDto<Integer> hitProduct(@RequestBody HitDto hitDto) {
		memberService.hitProduct(hitDto);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	/*
	 * 리뷰작성
	 */
	@PostMapping("/review/save/{pno}")
	public ResponseDto<Integer> saveReview(@RequestBody ReviewDto reviewDto) {
		reviewService.saveReview(reviewDto);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	/*
	 * 리뷰수정
	 */
	@PutMapping("/review/modify/{pno}/{rvno}")
	public ResponseDto<Integer> modifyReview(@RequestBody ReviewDto reviewDto) {
		reviewService.modifyReview(reviewDto);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	/*
	 * 리뷰삭제1
	 */
	@DeleteMapping("/review/delete/{pno}/{rvno}")
	public ResponseDto<Integer> deleteReview1(@RequestBody ReviewDto reviewDto) {
		reviewService.deleteReview1(reviewDto);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	/*
	 * 리뷰삭제2
	 */
	@DeleteMapping("/review/delete/{rvno}")
	public ResponseDto<Integer> deleteReview2(@RequestBody ReviewDto reviewDto) {
		reviewService.deleteReview2(reviewDto);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}	
	
	/*
	 * 리뷰 좋아요
	 */
	@PutMapping("/review/hit/{pno}/{rvno}")
	public ResponseDto<Integer> hitReview(@RequestBody HitDto hitDto) {
		reviewService.hitReview(hitDto);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	/*
	 * 리뷰답글작성
	 */
	@PostMapping("/review/save/{pno}/reply/{rvno}")
	public ResponseDto<Integer> saveReviewReply(@RequestBody ReviewDto reviewDto) {
		reviewService.saveReviewReply(reviewDto);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	/*
	 * 좋아요취소
	 */
	@DeleteMapping("/hit/delete/{hno}")
	public ResponseDto<Integer> deleteHit(@RequestBody HitDto hitDto) {
		memberService.deleteHit(hitDto);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	/*
	 * 문의작성
	 */
	@PostMapping("/product/qna/save/{pno}")
	public ResponseDto<Integer> saveQuestoin(@RequestBody QnADto qnaDto) {
		qnaService.saveQuestion(qnaDto);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	/*
	 * 문의답변
	 */
	@PutMapping("/product/qna/save/{pno}/response")
	public ResponseDto<Integer> saveResponse(@RequestBody QnADto qnaDto) {
		qnaService.saveResponse(qnaDto);
		qnaService.modifyStatus(qnaDto); // 주문상세 status 변경
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	/*
	 * 문의삭제
	 */
	@PutMapping("/product/qna/delete/{pno}/response")
	public ResponseDto<Integer> deleteResponse(@RequestBody QnADto qnaDto) {
		qnaService.deleteResponse(qnaDto);
		qnaService.undoStatus(qnaDto); // 주문상세 status 변경
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	/*
	 * 주문상태 수정
	 */
	@PutMapping("/product/modify/{odno}")
	public ResponseDto<Integer> modifyOrderStatus(@RequestBody OrdersDetailDto ordersDetailDto) {
		orderService.modifyOrderStatus(ordersDetailDto, ordersDetailDto.getStatus());
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	/*
	 * SNS유저 구매화면 정보저장
	 */
	@PutMapping("/member/modify/{id}")
	public ResponseDto<Integer> modifyInfo(Model model,
										@RequestBody MemberDto memberDto) {
		
		Member member = memberService.modifySnsMemberInfo(memberDto);		
		model.addAttribute("user", member);

		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);		
	}
	
	/*
	 * 비밀번호변경
	 */
	@PutMapping("/member/Pwdmodify/{pwd}")
	public ResponseDto<Integer> modifyPwd(Model model,
										@RequestBody MemberDto memberDto) {

		memberService.modifyPwd(memberDto);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);		
	}
}











