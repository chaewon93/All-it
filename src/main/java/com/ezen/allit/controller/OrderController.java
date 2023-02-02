package com.ezen.allit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.ezen.allit.domain.Member;

@Controller
@RequestMapping("/order/")
@SessionAttributes("member")
public class OrderController {
	
	@ModelAttribute("member")
	public Member setMember() {
		return new Member();
	}
	
	@RequestMapping("/orderList")
	public String insertOrder() {
		
		return "member/orderList";
	}

}
