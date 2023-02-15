package com.ezen.allit.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ezen.allit.dto.HitSaveRequestDto;
import com.ezen.allit.dto.OrdersDetailRequestDto;
import com.ezen.allit.dto.QnADto;
import com.ezen.allit.dto.ResponseDto;
import com.ezen.allit.dto.ReviewDeleteRequestDto;
import com.ezen.allit.dto.ReviewModifyRequestDto;
import com.ezen.allit.dto.ReviewReplySaveRequestDto;
import com.ezen.allit.dto.ReviewSaveRequestDto;
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
	public ResponseDto<Integer> hitProduct(@RequestBody HitSaveRequestDto hitSaveRequestDto) {
		memberService.hitProduct(hitSaveRequestDto);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	/*
	 * 리뷰작성
	 */
	@PostMapping("/review/save/{pno}")
	public ResponseDto<Integer> saveReview(@RequestBody ReviewSaveRequestDto reviewSaveRequestDto) {
		reviewService.saveReview(reviewSaveRequestDto);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	/*
	 * 리뷰수정
	 */
	@PutMapping("/review/modify/{pno}/{rvno}")
	public ResponseDto<Integer> modifyReview(@RequestBody ReviewModifyRequestDto reviewModifyRequestDto) {
		reviewService.modifyReview(reviewModifyRequestDto);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	/*
	 * 리뷰삭제
	 */
	@DeleteMapping("/review/delete/{pno}/{rvno}")
	public ResponseDto<Integer> deleteReview(@RequestBody ReviewDeleteRequestDto reviewDeleteRequestDto) {
		reviewService.deleteReview(reviewDeleteRequestDto);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	/*
	 * 리뷰 좋아요
	 */
	@PutMapping("/review/hit/{pno}/{rvno}")
	public ResponseDto<Integer> hitReview(@RequestBody HitSaveRequestDto hitSaveRequestDto) {
		reviewService.hitReview(hitSaveRequestDto);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	/*
	 * 리뷰답글작성
	 */
	@PostMapping("/review/save/{pno}/reply/{rvno}")
	public ResponseDto<Integer> saveReviewReply(@RequestBody ReviewReplySaveRequestDto reviewSaveRequestDto) {
		reviewService.saveReviewReply(reviewSaveRequestDto);
		
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
		qnaService.modifyStatus(qnaDto);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	/*
	 * 문의삭제
	 */
	@PutMapping("/product/qna/delete/{pno}/response")
	public ResponseDto<Integer> deleteResponse(@RequestBody QnADto qnaDto) {
		qnaService.saveResponse(qnaDto);
		qnaService.undoStatus(qnaDto);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	/*
	 * 주문상태 수정
	 */
	@PutMapping("/product/modify/{odno}")
	public ResponseDto<Integer> modifyOrderStatus(@RequestBody OrdersDetailRequestDto detailRequestDto) {
		System.out.println("detailRequestDto = " + detailRequestDto);
		orderService.modifyOrderStatus(detailRequestDto, detailRequestDto.getStatus());
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	/*
	 * 상품수정
	 */
//	@PostMapping("/seller/modify/{pno}")
//	public ResponseDto<Integer> modifyProduct(@PathVariable int pno,
//											@RequestBody Product product, MultipartFile imageFile) throws Exception {
//		productService.modifyProduct(pno, product, imageFile);
//		
//		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1); 
//	}
}












