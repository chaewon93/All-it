package com.ezen.allit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezen.allit.domain.Seller;
import com.ezen.allit.service.MemberService;
import com.ezen.allit.service.SellerService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {
	private final MemberService memberService;
	private final SellerService sellerService;
	
	/** 홈 화면 이동 */
	@GetMapping({"", "/"})
	public String index() {
		
		return "index";
	}

	/** 회원가입 페이지 */
	@GetMapping("/member-join")
	public String joinView() {
		return "member/join";
	}
	
	/** 회원가입 기능 처리 */
/*	
	@PostMapping("/member-join")
	public String join(Member member, SessionStatus status) {
		member.setGrade(Grade.BRONZE);
		memberService.saveMember(member);
		status.setComplete();
		
		couponService.insertMemCoupon(member, 1);
		
		return "redirect:/member-login";
	}
*/	
	
	/** 아이디 중복 확인 처리 */
	@ResponseBody
	@PostMapping("/member-idCheck")
	public int memberIdCheck(@RequestParam("userId") String user_id) {
		return memberService.idCheck(user_id);
	}
	
	/** 사용자 로그인 페이지 */
	@GetMapping("/member-login")
	public String memberLoginView() {
		return "member/login";
	}
	
	/** 아이디/비밀번호 찾기 창 */
	@GetMapping("/findIdAndPw")
	public String findForm() {
		return "member/findIdAndPw";
	}

	// 판매자 입점신청 화면 이동
	@GetMapping("/sellerApply")
	public String applyView() {
		
		return "seller/apply";
	}
	
	/** 판매자 입점신청 */
	@PostMapping("/sellerApply")
	public String apply(Seller seller) {
		sellerService.saveSeller(seller);
		
		return "redirect:/";

	}
	
	/** 판매자 아이디 중복확인 */
	@ResponseBody
	@PostMapping("/sellerIdCheck")
	public int sellerIdCheck(@RequestParam("userId") String user_id) {
		return sellerService.idCheck(user_id);
	}
	
	/** 판매자 로그인 화면 이동 */
	@GetMapping("/sellerLogin")
	public String loginView() {
		
		return "seller/login";
	}
	
	/** 권한없는 페이지 요청시 */
	@GetMapping("/denied")
	public String loginError() {
		
		return "denied";
	}
}
