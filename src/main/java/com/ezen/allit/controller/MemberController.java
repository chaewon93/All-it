package com.ezen.allit.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ezen.allit.config.auth.PrincipalDetailMember;
import com.ezen.allit.domain.Coupon;
import com.ezen.allit.domain.Hit;
import com.ezen.allit.domain.MemCoupon;
import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.OrdersDetail;
import com.ezen.allit.domain.QnA;
import com.ezen.allit.domain.Review;
import com.ezen.allit.dto.ReviewDto;
import com.ezen.allit.repository.MemberRepository;
import com.ezen.allit.repository.OrdersDetailRepository;
import com.ezen.allit.repository.ProductRepository;
import com.ezen.allit.service.CouponService;
import com.ezen.allit.service.MemberService;
import com.ezen.allit.service.OrderService;

@Controller
@RequestMapping("/member/")
@SessionAttributes("user")
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private CouponService couponService;
	
	@Autowired
	private OrderService orderService;
	
	@Autowired
	private MemberRepository memberRepo;
	
	@Autowired
	private ProductRepository proRepo;
	
	@Autowired
	private OrdersDetailRepository ordersDetailRepo;
	
	@ModelAttribute("user")
	public Member setMember(@AuthenticationPrincipal PrincipalDetailMember principal) {
		if(principal != null) {
			Member member = principal.getMember();
			System.out.println("member=============== = " + member);
			return member;
		} else {
			return null;
		}
	}
	
	/** 메인 페이지 */
/*	@GetMapping("/index")
	public String index() {
		return "index";
	}
*/	

	/** 로그인 기능 처리 => Security 적용 */ 
//	@PostMapping("/login")
//	public String login(Member member, Model model) {
//		
//		Member findMember = memberService.getMember(member);
//		
//		System.out.println("findMember is " + findMember);
//		System.out.println("[login()] findMember : " + findMember);
//		//System.out.println("입력한 비밀번호 : "+member.getPwd());
//		//System.out.println(findMember.getMemCoupon());
//		if(findMember != null && findMember.getPwd().equals(member.getPwd())) {
//			model.addAttribute("user", findMember);
//
//			// 비밀번호 일치시 메인 화면으로 이동
//			return "redirect:/";
//			
//		} else {
//			System.out.println("[Session Check] login fail : "+member);
//			// 비밀번호 불일치시 로그인 화면으로 이동
//			return "redirect:/member-login";
//		}
//	}

	
	/** 아이디 찾기 기능 처리 */
	@PostMapping("/findId")
	public String findId(Model model, Member member) {
		Member findMember = memberService.findById(member);
		//System.out.println("[findId] 아이디 " +id);

		if (findMember != null) {  // 이름과 이메일을 조건으로 아이디 조회 성공
			model.addAttribute("message", 1);
			model.addAttribute("id", findMember.getId());
		} else {
			model.addAttribute("message", -1);
		}
		
		return "member/findId";
	}
	
	/** 비밀번호 찾기 기능 처리 */
	@PostMapping("/findPw")
	public String findPw(Model model, Member member) {
		Member findMember = memberService.findByPw(member);
		//System.out.println("[findPw] 비밀번호 " +findMember.getPwd());

		if (findMember != null) {  // 이름과 이메일을 조건으로 아이디 조회 성공
			model.addAttribute("message", 1);
			model.addAttribute("pw", findMember.getPwd());
			model.addAttribute("id", findMember.getId());

			return "member/findPw";
		} else {
			model.addAttribute("message", -1);
			
			return "member/findPwError";
		}
		
	}
	
	/** 비밀번호 수정 처리 */
	@PostMapping("/modifyPwd")
	public String modifyPwd(Member member, Model model) {
		System.out.println("[Member infoModify()] Member : "+member);

		memberService.modifyMemberPwd(member);			

		// 세션에 수정된 정보 저장
		model.addAttribute("user", memberRepo.findById(member.getId()).get());
		
		return "redirect:/";
	}
	
	/** 비밀번호변경 창 */
	@GetMapping("/modifyPwdInfo")
	public String modifyForm() {

		return "member/modifyPwd";
	}
	
	/** SNS사용자 구매화면에서 정보저장 창 */
	@GetMapping("/infoWrite/{mid}")
	public String getInfoForm(Model model, @PathVariable String mid) {
		model.addAttribute("id", mid);
		return "member/infoWrite";
	}
	
	/** 로그아웃 처리 => Security 적용 */
//	@GetMapping("/logout")
//	public String logout(SessionStatus status) {
//		status.setComplete();	// 세션 데이터 삭제 및 세션 해지
//		
//		return "redirect:/";
//	}
	
	/** 마이 페이지(내 정보 확인) */
	@GetMapping("/info")
	public void info(Model model) {
		Member member = (Member) model.getAttribute("user");
		System.out.println("member = " + member);

		String fullAddr = member.getAddress();
		//System.out.println("[Member info()] user Address : "+fullAddr);
		if(fullAddr != null) {
			String[] addr = fullAddr.split(",");
			model.addAttribute("addr", addr);
		}
		
		// 세션에 수정된 정보 저장
		model.addAttribute("user", memberRepo.findById(member.getId()).orElse(member));
		
	}
	
	/** 내 정보 수정 처리 */
	@PostMapping("/infoModify")
	public String infoModify(Member member, Model model) {
		System.out.println("[Member infoModify()] Member : "+member);
		
		// 회원 정보 수정
		if(member.getProvider() == "") { // 일반회원 정보수정시
			memberService.modifyMember(member);			
		} else {						 // sns회원 정보수정시
			memberService.modifySnsMember(member);
		}
		
		// 세션에 수정된 정보 저장
		model.addAttribute("user", memberRepo.findById(member.getId()).get());
		
		return "redirect:/";
	}
	
	/** 회원 탈퇴 처리 */
	@PostMapping("/userDel")
	public String userDel(Member member) {
		System.out.println("[Member userDel()] Member : "+member);
		memberService.deleteMember(member.getId());
		SecurityContextHolder.clearContext();
		
		return "redirect:/";
	}
	/*
	 * @ResponseBody
	 * @PostMapping("/userDel") 
	 * public String userDel(@RequestParam("userId") String user_id) { 
	 * 	memberService.deleteMember(user_id);
	 * 	return "redirect:index"; 
	 * }
	 */

	/** 마이올잇>문의하기(1:1문의) - QnA 글 작성 */
	@GetMapping("/qna")
	public String QnaView() {
		return "mypage/qnaWrite";
	}
	
	/** 마이올잇>문의하기(1:1문의) - QnA 글 작성 처리 */
	@PostMapping("/writeQna")
	public String writeQna(QnA qna,
						@AuthenticationPrincipal PrincipalDetailMember principal) {
	
		qna.setStatus(0);
		qna.setMember(principal.getMember());
		System.out.println("[Member writeQna()] qna : "+qna);
		System.out.println("[Member writeQna()] qna writer : "+principal.getMember());
		
		memberService.saveQna(qna);
		
		return "redirect:qnaList";
	}
	
	/** 마이올잇>문의내역 조회 */
	@RequestMapping("/qnaList")
	public String getQnaList(Model model,
							@AuthenticationPrincipal PrincipalDetailMember principal,
							@PageableDefault(page = 1) Pageable pageable) {
		
		Page<QnA> qnaList = memberService.getQnaList(principal.getMember(), pageable);
		System.out.println("[Member getQnaList()] qnaList : "+qnaList.getTotalElements());
		
		int naviSize = 10; // 페이지네이션 갯수
		int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / naviSize))) - 1) * naviSize + 1; // 1 11 21 31 ~~
		int endPage = ((startPage + naviSize - 1) < qnaList.getTotalPages()) ? startPage + naviSize - 1 : qnaList.getTotalPages();

		model.addAttribute("list", qnaList);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);	
		model.addAttribute("url", "/member/qnaList");
		if(qnaList.getTotalElements() == 0) model.addAttribute("size", 0);
		
		return "mypage/qnaList";
	}
	
	/** 마이올잇>문의내역 상세보기 */
	@GetMapping("/qnaDetail")
	public String getQnaDetail(Model model, int qno) {
		QnA qna = memberService.getQnaDetail(qno);
		model.addAttribute("qna", qna);
		
		return "mypage/qnaDetail";
	}
	
	/** 올잇머니 충전 */
	@ResponseBody
	@PostMapping("/moneyCharge")
	public void moneyCharge(@ModelAttribute("user") Member member,
						@RequestBody Map<String, Object> map, Model model) {

		memberService.addMoney(member.getId(), Integer.parseInt(map.get("money").toString()));
		
		// 세션에 수정된 정보 저장
		model.addAttribute("user", memberService.getMember(member));
	}
	
	/** 주문 취소/교환/반품 내역 */
	@GetMapping("/cancelList")
	public String cancelList(Model model, @ModelAttribute("user") Member member,
						@PageableDefault(page = 1) Pageable pageable,
						@RequestParam(value = "status", defaultValue = "cancel") String status) {
		
		// 주문취소 내역
		Page<OrdersDetail> cancelList = orderService.getCancelList(member, 5, pageable);
		// 교환 내역
		Page<OrdersDetail> exchangeList = orderService.getExchangeAndRefundList(member, 6, 9, pageable);
		// 반품 내역
		Page<OrdersDetail> refundList = orderService.getExchangeAndRefundList(member, 7, 10, pageable);
		
//		int naviSize = 10; // 페이지네이션 갯수
//		int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / naviSize))) - 1) * naviSize + 1; // 1 11 21 31 ~~
//		int cancel_endPage = ((startPage + naviSize - 1) < cancelList.getTotalPages()) ? startPage + naviSize - 1 : cancelList.getTotalPages();
//		int exchange_endPage = ((startPage + naviSize - 1) < exchangeList.getTotalPages()) ? startPage + naviSize - 1 : exchangeList.getTotalPages();
//		int refund_endPage = ((startPage + naviSize - 1) < refundList.getTotalPages()) ? startPage + naviSize - 1 : refundList.getTotalPages();
		
//		model.addAttribute("list", cancelList);
//		model.addAttribute("startPage", startPage);
//		model.addAttribute("cancel_endPage", cancel_endPage);
//		model.addAttribute("exchange_endPage", exchange_endPage);
//		model.addAttribute("refund_endPage", refund_endPage);
		model.addAttribute("url", "/member/cancelList");	
		
		model.addAttribute("cancelList", cancelList);
		model.addAttribute("exchangeList", exchangeList);
		model.addAttribute("refundList", refundList);
		
		System.out.println("======> status : " +status);
		model.addAttribute("status", status);
		
		return "mypage/cancelList";
	}

	/** 교환 내역 */
//	@ResponseBody
//	@GetMapping("/exchangeList")
//	public String exchangeList(Model model, @ModelAttribute("user") Member member,
//						@PageableDefault(page = 1) Pageable pageable) {
//		System.out.println("=================교 환 내 역 조 회======================");
//		// 교환 내역
//		Page<OrdersDetail> exchangeList = orderService.getCancelList(member, 6, pageable);
//		
//		int naviSize = 10; // 페이지네이션 갯수
//		int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / naviSize))) - 1) * naviSize + 1; // 1 11 21 31 ~~
//		int endPage = ((startPage + naviSize - 1) < exchangeList.getTotalPages()) ? startPage + naviSize - 1 : exchangeList.getTotalPages();
//		
//		model.addAttribute("list", exchangeList);
//		model.addAttribute("startPage", startPage);
//		model.addAttribute("endPage", endPage);	
//		model.addAttribute("url", "/member/cancelList");	
//		
//		model.addAttribute("exchangeList", exchangeList);
//
//		int size = 0;
//		if(exchangeList.getTotalElements() != 0) {
//			size = (int) exchangeList.getTotalElements();
//		}
//		model.addAttribute("size", size);
//		
//		return "mypage/cancelList";
//	}
	
	/** 반품 내역 */
//	@GetMapping("/refundList")
//	public String refundList(Model model, @ModelAttribute("user") Member member,
//							@PageableDefault(page = 1) Pageable pageable) {
//		System.out.println("=================반 품 내 역 조 회======================");
//		// 반품 내역
//		Page<OrdersDetail> refundList = orderService.getCancelList(member, 7, pageable);
//
//		int naviSize = 10; // 페이지네이션 갯수
//		int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / naviSize))) - 1) * naviSize + 1; // 1 11 21 31 ~~
//		int endPage = ((startPage + naviSize - 1) < refundList.getTotalPages()) ? startPage + naviSize - 1 : refundList.getTotalPages();
//
//		model.addAttribute("list", refundList);
//		model.addAttribute("startPage", startPage);
//		model.addAttribute("endPage", endPage);	
//		model.addAttribute("url", "/member/cancelList");	
//
//		model.addAttribute("refundList", refundList);
//
//		int size = 0;
//		if(refundList.getTotalElements() != 0) {
//			size = (int) refundList.getTotalElements();
//		}
//		model.addAttribute("size", size);
//		
//		return "mypage/cancelList";
//	} 
	
	/** 쿠폰조회 팝업창 */
	@GetMapping("coupon")
	public String coupon(@ModelAttribute("user") Member member, Model model, 
						@RequestParam(value="pno", defaultValue = "0")int pno) {

		List<MemCoupon> memCouList = new ArrayList<>();
		System.out.println("11111111111111");
		
		if(member != null) {
			if(pno == 0) {
				// 내가 가지고 있는 쿠폰 조회
				memCouList = member.getMemCoupon();
				model.addAttribute("list", memCouList);
				System.out.println("======================== getmemcoupon");
				System.out.println(memCouList);
			}else {
				memCouList = couponService.MemProCouponList(member, pno);
				model.addAttribute("list", memCouList);
			}
			
			List<Coupon> couList = couponService.forMemberCouponList(member, pno);
			System.out.println("22222");
	
			model.addAttribute("pno", pno);
			
			int price = 0;
			if(pno != 0) {
				 price = proRepo.findById(pno).get().getPrice();
			}else {
				price = 0;
			}
			model.addAttribute("price", price);
	
			List<Coupon> couponList = new ArrayList<>();
			for(MemCoupon memCou : memCouList) {
				couponList.add(memCou.getCoupon());
			}
			System.out.println("33333333333333333");
			
			couList.removeAll(couponList);
	
			System.out.println("444444444444444");
			model.addAttribute("couList", couList);
		} else {
			model.addAttribute("login", "noLogin");
		}
		
		return "member/coupon";
	}
	
	/** 마이올잇>할인쿠폰 */
	@GetMapping("coupon1")
	public String coupon1(@ModelAttribute("user") Member member, Model model, 
						@RequestParam(value="pno", defaultValue = "0") int pno) {

		List<MemCoupon> memCouList = new ArrayList<>();
		if(pno == 0) {
			memCouList = member.getMemCoupon();
			model.addAttribute("list", memCouList);
			System.out.println("======================== getmemcoupon");
			System.out.println(memCouList);
		}else {
			memCouList = couponService.MemProCouponList(member, pno);
			model.addAttribute("list", memCouList);
		}
		List<Coupon> couList = couponService.forMemberCouponList(member, pno);

		model.addAttribute("pno", pno); 
		int price = 0;
		if(pno != 0) {
			 price = proRepo.findById(pno).get().getPrice();
		}else {
			price = 0;
		}
		
		model.addAttribute("price", price);

		List<Coupon> couponList = new ArrayList<>();
		for(MemCoupon memCou : memCouList) {
			couponList.add(memCou.getCoupon());
		}

		couList.removeAll(couponList);

		model.addAttribute("couList", couList);
		
		return "mypage/coupon";
	}
	
	@PostMapping("downCoupon")
	public String downCoupon(@ModelAttribute("user") Member member,
			@RequestParam Map<String, Object> map, RedirectAttributes re) {
		
		int couId = Integer.parseInt(String.valueOf(map.get("couId")));
		
		couponService.insertMemCoupon(member, couId);
		
		return "redirect:coupon";
	}
	
	/** 마이올잇>리뷰관리 */
	@GetMapping("/reviewList")
	public String reviewView(Model model,
							@PageableDefault(page = 1) Pageable pageable,
							@AuthenticationPrincipal PrincipalDetailMember principal) {
	
		Page<Review> reviewList = memberService.getReviewList(principal.getMember().getId(), pageable);
		
		int naviSize = 10; // 페이지네이션 갯수
		int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / naviSize))) - 1) * naviSize + 1; // 1 11 21 31 ~~
		int endPage = ((startPage + naviSize - 1) < reviewList.getTotalPages()) ? startPage + naviSize - 1 : reviewList.getTotalPages();

		model.addAttribute("list", reviewList);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);	
		model.addAttribute("url", "/member/reviewList");	
		model.addAttribute("reviewList", reviewList);
		if(reviewList.getTotalElements() == 0) model.addAttribute("size", 0);
		
		return "mypage/reviewList";
	}
	
	/** 마이올잇>리뷰작성 */
	@GetMapping("/reviewWrite/{odno}")
	public String reviewWriteView(Model model,
								@PathVariable int odno) {

		OrdersDetail ordersDetail = ordersDetailRepo.findById(odno).get();
		model.addAttribute("ordersDetail", ordersDetail);
		
		return "mypage/reviewWrite";
	}
	
	/** 마이올잇>리뷰작성 */
	@PostMapping("/writeReview")
	public String writeReview(ReviewDto reviewDto) throws Exception {
		System.out.println("reviewDto1 = " + reviewDto);
		memberService.saveReview(reviewDto);

		return "redirect:reviewList";
	}
	
	/** 마이올잇>좋아요리스트 */
	@GetMapping("/likeList")
	public String likeView(Model model,
							@PageableDefault(page = 1) Pageable pageable,
							@AuthenticationPrincipal PrincipalDetailMember principal) {
	
		Page<Hit> likeList = memberService.getLikeList(principal.getMember().getId(), pageable);
		
		int naviSize = 10; // 페이지네이션 갯수
		int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / naviSize))) - 1) * naviSize + 1; // 1 11 21 31 ~~
		int endPage = ((startPage + naviSize - 1) < likeList.getTotalPages()) ? startPage + naviSize - 1 : likeList.getTotalPages();

		model.addAttribute("list", likeList);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);	
		model.addAttribute("url", "/member/likeList");	
		model.addAttribute("likeList", likeList);
		if(likeList.getTotalElements() == 0) model.addAttribute("size", 0);
		
		return "mypage/likeList";
	}
}
