package com.ezen.allit.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.ezen.allit.domain.Cart;
import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.Orders;
import com.ezen.allit.domain.Product;
import com.ezen.allit.repository.CartRepository;

@Controller
@RequestMapping("/order/")
@SessionAttributes("user")
public class OrderController {
	
	@Autowired
	private CartRepository cartRepo;
	
	@ModelAttribute("user")
	public Member setMember() {
		return new Member();
	}
	
	/** 주문/결제 페이지 요청 */
	@PostMapping("/orderInfo")
	public String ordersView(Model model, @ModelAttribute("user") Member member,
							@RequestParam(value = "cno") int[] cno) {
		
		System.out.println("[Orders ordersView()] cartList.size : "+cno.length);
		
		int totalPrice = 0;
		List<Cart> cartList = new ArrayList<>();
		
 		for(int i=0; i<cno.length; i++) {
			Cart cart = cartRepo.findById(cno[i]).get();
			cartList.add(cart);
			
			totalPrice += cart.getProduct().getPrice() * cart.getQuantity();
		}
 		
 		model.addAttribute("cartList", cartList);
		model.addAttribute("totalPrice", totalPrice);
		System.out.println("[Orders ordersView()] totalPrice : "+totalPrice);
		
		return "mypage/orderInfo";
	}
	
	/** 주문하기 */
	@PostMapping("/orders")
	public String insertOrder(Orders order, @ModelAttribute("user") Member member) {
		
		return "redirect:orderList";
	}
	
	/** 주문 목록 */
	@RequestMapping("/orderList")
	public String getOrderList() {
		
		return "mypage/orderList";
	}

}
