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

	/** 회원가입 페이지
	 * @author 임채원
	 * @return 회원가입 페이지로 이동
	 */
	@GetMapping("/member-join")
	public String joinView() {
		return "member/join";
	}
	
	/** 회원가입 기능 처리 
	 * @author 임채원
	 * @param member	회원가입 폼에서 입력한 회원 정보가 담긴 객체
	 * @return			로그인 페이지로 이동
	 */
	@PostMapping("/member-join")
	public String join(Member member) {
		memberService.saveMember(member);
		couponService.insertMemCoupon(member, 1);
		
		return "redirect:/member-login";
	}

	/** 아이디 중복 확인 처리 - 사용자, 판매자 모두 조회
	 * @author 임채원
	 * @param user_id	회원가입 페이지에서 입력한 아이디
	 * @return			0:사용불가 아이디 / -1:사용가능 아이디
	 */
	@ResponseBody
	@PostMapping("/idCheck")
	public int idCheck(@RequestParam("userId") String user_id) {
		
		// 0 : 이미 사용중, -1 : 사용 가능
		int member_id_chk = memberService.idCheck(user_id);
		
		// member 테이블에서 사용 가능한 아이디인 경우 seller 테이블 체크
		if(member_id_chk == -1) {
			// 0 : 이미 사용중, -1 : 사용 가능
			int seller_id_chk = sellerService.idCheck(user_id);
			
			if(seller_id_chk == -1) {	// member, seller 테이블 모두에서 사용 가능한 아이디
				return seller_id_chk;
			} else {					// seller 테이블에서 중복이라 사용 불가한 아이디
				return 0;
			}
		} else {						// member 테이블에서 중복이라 사용 불가한 아이디
			return 0;
		}
	}
	
	/** 사용자 로그인 페이지
	 * @author 임채원
	 * @return 로그인 페이지로 이동
	 */
	@GetMapping("/member-login")
	public String memberLoginView() {
		return "member/login";
	}
	
	/** 아이디/비밀번호 찾기 팝업창
	 * @author 임채원
	 * @return 아이디/비밀번호 찾기 페이지로 이동
	 */
	@GetMapping("/findIdAndPw")
	public String findForm() {
		System.out.println("개짱나네!!!");
		return "member/findIdAndPw";
	}

	/**
	 * 판매자 입점신청 화면 이동
	 * @author 정동욱
	 * @return 판매자 입점신청 페이지로 이동
	 */
	@GetMapping("/sellerApply")
	public String applyView() {
		
		return "seller/apply";
	}
	
	/** 판매자 입점신청 기능 처리
	 * @author 정동욱
	 * @param seller	입점신청 페이지에서 입력한 판매자 정보가 담긴 객체
	 * @return			메인 페이지로 이동
	 */
	@PostMapping("/sellerApply")
	public String apply(Seller seller) {
		sellerService.saveSeller(seller);
		
		return "redirect:/";

	}
	
	/** 판매자 로그인 화면 이동 => Security 적용
	 * @author 정동욱
	 * @return 판매자 로그인 페이지로 이동
	 */
//	@GetMapping("/sellerLogin")
//	public String loginView() {
//		
//		return "seller/login";
//	}
	
	/** 권한없는 페이지 요청시
	 * @author 정동욱
	 * @return 
	 */
	@GetMapping("/denied")
	public String loginError() {
		
		return "common/denied";
	}
	
	/** footer : 이용약관
	 * @author 정동욱
	 * @return	사이트 이용약관을 볼 수 있는 페이지로 이동
	 */
	@GetMapping("/terms-of-service")
	public String termsOfService() {
		
		return "common/terms_of_service";
	}
	
	/** footer : 개인정보 처리방침
	 * @author 정동욱
	 * @return 개인정보 처리방침을 볼 수 있는 페이지로 이동
	 */
	@GetMapping("/privacy")
	public String privacy() {
		
		return "common/privacy";
	}
}
