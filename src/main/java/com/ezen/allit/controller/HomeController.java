package com.ezen.allit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	// 홈 화면 이동
	@GetMapping({"", "/"})
	public String index() {
		
		return "index";
	}
	
	/** 사용자 로그인 페이지 */
	@GetMapping("/member-login")
	public String loginView() {
		return "member/login";
	}
	
	/** 아이디/비밀번호 찾기 창 */
	@GetMapping("/findIdAndPw")
	public String findForm() {
		return "member/findIdAndPw";
	}

	/** 회원가입 페이지 */
	@GetMapping("/member-join")
	public String joinView() {
		return "member/join";
	}
}
