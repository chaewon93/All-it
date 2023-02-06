package com.ezen.allit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

import com.ezen.allit.domain.MemCoupon;
import com.ezen.allit.domain.Grade;
import com.ezen.allit.domain.Member;
import com.ezen.allit.service.CouponService;
import com.ezen.allit.service.MemberService;

@Controller
@RequestMapping("/member/")
@SessionAttributes("user")
public class MemberController {
	
	@Autowired
	private MemberService memberService;
	
	@Autowired
	private CouponService couponService;
	
	/** 메인 페이지 */
/*	@GetMapping("/index")
	public String index() {
		return "index";
	}
*/	
		
	/** 로그인 기능 처리 */
	@PostMapping("/login")
	public String login(Member member, Model model) {
		
		Member findMember = memberService.getMember(member);
		
		System.out.println("findMember is " + findMember);
		System.out.println("[login()] findMember : " + findMember);
		//System.out.println("입력한 비밀번호 : "+member.getPwd());
		System.out.println(findMember.getMemCoupon());
		if(findMember != null && findMember.getPwd().equals(member.getPwd())) {
			model.addAttribute("user", findMember);

			// 비밀번호 일치시 메인 화면으로 이동
			return "redirect:/";
			
		} else {
			System.out.println("[Session Check] login fail : "+member);
			// 비밀번호 불일치시 로그인 화면으로 이동
			return "redirect:/member-login";
		}
	}
	
	/** 아이디 찾기 기능 처리 */
	@PostMapping("/findId")
	public String findId(Member member, Model model) {
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
	public String findPw(Member member, Model model) {
		Member findMember = memberService.findByPw(member);
		//System.out.println("[findPw] 비밀번호 " +findMember.getPwd());

		if (findMember != null) {  // 이름과 이메일을 조건으로 아이디 조회 성공
			model.addAttribute("message", 1);
			model.addAttribute("pw", findMember.getPwd());
		} else {
			model.addAttribute("message", -1);
		}
		
		return "member/findPw";
	}
	
	
	/** 회원가입 기능 처리 */
	@PostMapping("/join")
	public String join(Member member, SessionStatus status) {
		member.setGrade(Grade.BRONZE);
		memberService.saveMember(member);
		status.setComplete();
		
		couponService.insertMemCoupon(member, 1);
		
		return "redirect:/member-login";
	}
	
	/** 아이디 중복 확인 처리 */
	@ResponseBody
	@PostMapping("/idCheck")
	public int idCheck(@RequestParam("userId") String user_id) {
		return memberService.idCheck(user_id);
	}
	
	/** 로그아웃 처리 */
	@GetMapping("/logout")
	public String logout(SessionStatus status) {
		status.setComplete();	// 세션 데이터 삭제 및 세션 해지
		
		return "redirect:/";
	}
	
	/** 마이 페이지(내 정보 확인) */
	@GetMapping("/info")
	public void info(@ModelAttribute("user") Member member, Model model) {
		String fullAddr = member.getAddress();
		System.out.println("[Member info()] user Address : "+fullAddr);
		if(fullAddr != null) {
			String[] addr = fullAddr.split(",");
			model.addAttribute("addr", addr);
		}
	}
	
	/** 내 정보 수정 처리 */
	@PostMapping("/infoModify")
	public String infoModify(Member member, Model model) {
		System.out.println("[Member infoModify()] Member : "+member);
		
		// 회원 정보 수정
		memberService.saveMember(member);
		
		// 세션에 수정된 정보 저장
		Member findMember = memberService.getMember(member);
		model.addAttribute("user", findMember);
		
		return "redirect:/";
	}
	
	/** 회원 탈퇴 처리 */
	@PostMapping("/userDel")
	public String userDel(Member member, SessionStatus status) {
		System.out.println("[Member userDel()] Member : "+member);
		status.setComplete();	// 세션 데이터 삭제 및 세션 해지
		memberService.deleteMember(member.getId());
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
	
	@GetMapping("coupon")
	public String coupon(Member member, Model model) {
		List<MemCoupon> memCouList = member.getMemCoupon();
		model.addAttribute("list", memCouList);
		
		
		System.out.println("-----------------");
		System.out.println(memCouList);
		System.out.println("-----------------");
		
		return "member/coupon";
	}
}
