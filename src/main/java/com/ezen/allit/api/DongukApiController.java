package com.ezen.allit.api;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
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

	/** 상품삭제
	 * @author	  정동욱
	 * @param pno 판매자 상품수정 페이지에서 넘어온 상품번호
	 * @return 	  ajax 사용. 페이지 이동이 아닌 매서드 처리가 성공했음을 알림
	 */
	@DeleteMapping("/seller/product/delete/{pno}")
	public ResponseDto<Integer> deleteProduct(@PathVariable int pno) {
		sellerService.deleteProduct(pno);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	/** 상품 좋아요
	 * @author	  	 정동욱
	 * @param hitDto 상품 페이지에서 좋아요 클릭시 넘어온 데이터를 받을 Dto 객체
	 * @return 	  	 ajax 사용. 페이지 이동이 아닌 매서드 처리가 성공했음을 알림
	 */
	@PutMapping("/product/hit/{pno}")
	public ResponseDto<Integer> hitProduct(@RequestBody HitDto hitDto) {
		memberService.hitProduct(hitDto);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	/** 댓글작성
	 * @author	  	 	정동욱
	 * @param reviewDto 상품 페이지에서 댓글작성 시 넘어온 데이터를 받을 Dto 객체
	 * @return 	  	 	ajax 사용. 페이지 이동이 아닌 매서드 처리가 성공했음을 알림 
	 */
//	@PostMapping("/review/save/{pno}")
//	public ResponseDto<Integer> saveReview(@RequestBody ReviewDto reviewDto) {
//		reviewService.saveReview(reviewDto);
//		
//		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
//	}
	
	/** 댓글수정
	 * @author	  	 	정동욱
	 * @param reviewDto 상품 페이지에서 댓글수정 시 넘어온 데이터를 받을 Dto 객체
	 * @return 	  	 	ajax 사용. 페이지 이동이 아닌 매서드 처리가 성공했음을 알림 
	 */
//	@PutMapping("/review/modify/{pno}/{rvno}")
//	public ResponseDto<Integer> modifyReview(@RequestBody ReviewDto reviewDto) {
//		reviewService.modifyReview(reviewDto);
//		
//		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
//	}
	
	/** 댓글삭제
	 * @author	  	 	정동욱
	 * @param reviewDto 상품 페이지에서 댓글삭제 시 넘어온 데이터를 받을 Dto 객체
	 * @return 	  	 	ajax 사용. 페이지 이동이 아닌 매서드 처리가 성공했음을 알림 
	 */
//	@DeleteMapping("/review/delete/{pno}/{rvno}")
//	public ResponseDto<Integer> deleteReview1(@RequestBody ReviewDto reviewDto) {
//		reviewService.deleteReview1(reviewDto);
//		
//		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
//	}
	
	/** 리뷰삭제
	 * @author	  	 	정동욱
	 * @param reviewDto 리뷰목록 페이지에서 리뷰삭제 시 넘어온 데이터를 받을 Dto 객체
	 * @return 	  	 	ajax 사용. 페이지 이동이 아닌 매서드 처리가 성공했음을 알림 
	 */
	@DeleteMapping("/review/delete/{rvno}")
	public ResponseDto<Integer> deleteReview2(@RequestBody ReviewDto reviewDto) {
		reviewService.deleteReview2(reviewDto);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}	
	
	/** 리뷰 좋아요
	 * @author	  	 정동욱
	 * @param hitDto 상품 페이지에서 리뷰좋아요 시 넘어온 데이터를 받을 Dto 객체
	 * @return 	  	 ajax 사용. 페이지 이동이 아닌 매서드 처리가 성공했음을 알림 
	 */
	@PutMapping("/review/hit/{pno}/{rvno}")
	public ResponseDto<Integer> hitReview(@RequestBody HitDto hitDto) {
		reviewService.hitReview(hitDto);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	/** 리뷰답글작성
	 * @author	  	    정동욱
	 * @param reviewDto 상품 페이지에서 리뷰답글 시 넘어온 데이터를 받을 Dto 객체
	 * @return 	  	    ajax 사용. 페이지 이동이 아닌 매서드 처리가 성공했음을 알림
	 */
//	@PostMapping("/review/save/{pno}/reply/{rvno}")
//	public ResponseDto<Integer> saveReviewReply(@RequestBody ReviewDto reviewDto) {
//		reviewService.saveReviewReply(reviewDto);
//		
//		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
//	}
	
	/** 좋아요취소
	 * @author	  	 정동욱
	 * @param hitDto 상품 페이지에서 리뷰/상품좋아요 2번째 클릭 시 넘어온 데이터를 받을 Dto 객체
	 * @return 	  	 ajax 사용. 페이지 이동이 아닌 매서드 처리가 성공했음을 알림 
	 */
	@DeleteMapping("/hit/delete/{hno}")
	public ResponseDto<Integer> deleteHit(@RequestBody HitDto hitDto) {
		memberService.deleteHit(hitDto);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	/** 문의작성
	 * @author	  	 정동욱
	 * @param qnaDto 문의작성 페이지에서 문의작성 시 넘어온 데이터를 받을 Dto 객체
	 * @return 	  	 ajax 사용. 페이지 이동이 아닌 매서드 처리가 성공했음을 알림 
	 */
	@PostMapping("/product/qna/save/{pno}")
	public ResponseDto<Integer> saveQuestoin(@RequestBody QnADto qnaDto) {
		qnaService.saveQuestion(qnaDto);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	/** 문의답변
	 * @author	  	 정동욱
	 * @param qnaDto 문의답변 페이지에서 문의답변 시 넘어온 데이터를 받을 Dto 객체
	 * @return 	  	 ajax 사용. 페이지 이동이 아닌 매서드 처리가 성공했음을 알림 
	 */
	@PutMapping("/product/qna/save/{pno}/response")
	public ResponseDto<Integer> saveResponse(@RequestBody QnADto qnaDto) {
		qnaService.saveResponse(qnaDto);
		qnaService.modifyStatus(qnaDto); // 주문상세 status 변경
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	/** 문의삭제
	 * @author	  	 정동욱
	 * @param qnaDto 문의목록 페이지에서 문의삭제 시 넘어온 데이터를 받을 Dto 객체
	 * @return 	  	 ajax 사용. 페이지 이동이 아닌 매서드 처리가 성공했음을 알림 
	 */
	@PutMapping("/product/qna/delete/{pno}/response")
	public ResponseDto<Integer> deleteResponse(@RequestBody QnADto qnaDto) {
		qnaService.deleteResponse(qnaDto);
		qnaService.undoStatus(qnaDto); // 주문상세 status 변경
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	/** 주문상태 수정
	 * @author	  	 		  정동욱
	 * @param ordersDetailDto 판매자 주문목록 페이지에서 배송정보 수정 시 넘어온 데이터를 받을 Dto 객체
	 * @return 	  	 		  ajax 사용. 페이지 이동이 아닌 매서드 처리가 성공했음을 알림 
	 */
	@PutMapping("/product/modify/{odno}")
	public ResponseDto<Integer> modifyOrderStatus(@RequestBody OrdersDetailDto ordersDetailDto) {
		orderService.modifyOrderStatus(ordersDetailDto, ordersDetailDto.getStatus());
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	/** SNS유저 구매화면 정보저장
	 * @author	  	    정동욱
	 * @param model 	정보수정 후 변경된 값을 다시 담을 객체
	 * @param memberDto 주문 페이지에서 주소지 정보수정 시 넘어온 데이터를 받을 Dto 객체
	 * @return 	  	 	ajax 사용. 페이지 이동이 아닌 매서드 처리가 성공했음을 알림 
	 */
	@PutMapping("/member/modify/{id}")
	public ResponseDto<Integer> modifyInfo(Model model,
										@RequestBody MemberDto memberDto) {
		
		Member member = memberService.modifySnsMemberInfo(memberDto);		
		model.addAttribute("user", member);

		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);		
	}
	
	/** 비밀번호변경
	 * @author	  	    정동욱
	 * @param model 	비밀번호변경 후 변경된 값을 다시 담을 객체
	 * @param memberDto 비밀번호변경 페이지에서 변경 시 넘어온 데이터를 받을 Dto 객체
	 * @return 	  	 	ajax 사용. 페이지 이동이 아닌 매서드 처리가 성공했음을 알림 
	 */
	@PutMapping("/member/Pwdmodify/{pwd}")
	public ResponseDto<Integer> modifyPwd(Model model,
										@RequestBody MemberDto memberDto) {

		Member member = memberService.modifyMemberPwd(memberDto);
		model.addAttribute("user", member);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);		
	}
	
	/** 회원탈퇴
	 * @author	  	    정동욱
	 * @param memberDto 마이페이지에서 삭제 시 넘어온 데이터를 받을 Dto 객체
	 * @return			비밀번호가 맞으면 1, 틀리면 0을 반환
	 */
	@DeleteMapping("/member/delete/{id}")
	public int deleteMember(@RequestBody MemberDto memberDto) {
		boolean match = memberService.checkPwd(memberDto);

		if(match) {
			memberService.deleteMember(memberDto.getId());
			SecurityContextHolder.clearContext();			
			return 1;		
		} else {
			return 0;
		}
	}
}











