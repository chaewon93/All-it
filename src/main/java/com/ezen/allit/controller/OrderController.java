package com.ezen.allit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.ezen.allit.domain.Cart;
import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.Orders;

@Controller
@RequestMapping("/order/")
@SessionAttributes("user")
public class OrderController {
	
	@ModelAttribute("user")
	public Member setMember() {
		return new Member();
	}
	
	/** 주문/결제 페이지 요청 */
	@PostMapping("/orderInfo")
	public String ordersView(Cart cart) {
		return "mypage/orderInfo";
	}
	
	/** 주문하기 */
	@PostMapping("/orders")
	public String insertOrder(Orders order, @ModelAttribute("user") Member member) {
		
		return "";
	}
	
	/** 주문 목록 */
	@RequestMapping("/orderList")
	public String getOrderList() {
		
		return "mypage/orderList";
	}

}
