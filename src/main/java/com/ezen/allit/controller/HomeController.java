package com.ezen.allit.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezen.allit.domain.CustomerCenter;
import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.Product;
import com.ezen.allit.domain.Seller;
import com.ezen.allit.service.CouponService;
import com.ezen.allit.service.CustomerCenterService;
import com.ezen.allit.service.MemberService;
import com.ezen.allit.service.ProductService;
import com.ezen.allit.service.SellerService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {
	private final MemberService memberService;
	private final SellerService sellerService;
	private final CouponService couponService;
	private final CustomerCenterService custoService;
	private final ProductService proService;;
	
	/** 홈 화면 이동 */
	@GetMapping({"", "/"})
	public String index(Model model) {
		// 메인화면에 출력되는 이벤트 리스트와 mdPick 리스트
		String pick ="1";
		List<CustomerCenter> eventList = custoService.findCustomerCenterByPick(pick);
		
		model.addAttribute("eventList", eventList);
		
		List<Product> proList = proService.getMdpickProductMainPage();
		
		model.addAttribute("proList", proList);
		
		return "index";
	}

	/** 회원가입 페이지 */
	@GetMapping("/member-join")
	public String joinView() {
		return "member/join";
	}
	
	/** 회원가입 기능 처리 */
	@PostMapping("/member-join")
	public String join(Member member) {
		memberService.saveMember(member);
		couponService.insertMemCoupon(member, 1);
		
		return "redirect:/member-login";
	}

	/** 아이디 중복 확인 처리 */
	@ResponseBody
	@PostMapping("/idCheck")
	public int idCheck(@RequestParam("userId") String user_id) {
		int result = 0;
		if(0 == memberService.idCheck(user_id)) {
			result = 0;
		} else if(0 == sellerService.idCheck(user_id)) {
			result = 0;
		} else {
			result = -1;
		}
		return result;
	}
	
	/** 사용자 로그인 페이지 */
	@GetMapping("/member-login")
	public String memberLoginView() {
		return "member/login";
	}
	
	/** 아이디/비밀번호 찾기 창 */
	@GetMapping("/findIdAndPw")
	public String findForm() {
		System.out.println("개짱나네!!!");
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
	
	/** 판매자 로그인 화면 이동 */
	@GetMapping("/sellerLogin")
	public String loginView() {
		
		return "seller/login";
	}
	
	/** 권한없는 페이지 요청시 */
	@GetMapping("/denied")
	public String loginError() {
		
		return "common/denied";
	}
	
	/** 푸터 : 이용약관 */
	@GetMapping("/terms-of-service")
	public String termsOfService() {
		
		return "common/terms_of_service";
	}
	
	/** 푸터 : 개인정보 처리방침 */
	@GetMapping("/privacy")
	public String privacy() {
		
		return "common/privacy";
	}
}














