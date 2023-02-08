package com.ezen.allit.controller;

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
import com.ezen.allit.repository.ProductRepository;
import com.ezen.allit.service.OrderService;
import com.ezen.allit.service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/order")
@SessionAttributes("user")
@RequiredArgsConstructor
public class OrderController {
	private final ProductRepository productRepo;
	private final OrderService orderService;
	
	@ModelAttribute("user")
	public Member setMember() {
		return new Member();
	}
	
	/** 주문/결제 페이지 요청 */
	@PostMapping("/orderInfo")
	public String getOrderView(Product product, Model model,
							@RequestParam("quantity") int quantity) {
		Product theProduct = productRepo.findById(product.getPno()).get();
		System.out.println("theProduct = " + theProduct);
		model.addAttribute("product", theProduct);
		model.addAttribute("quantity", quantity);
		
		return "mypage/orderInfo";
	}
	
	/** 주문하기 */
	@PostMapping("/orders")
	public String insertOrder(Product product, Member member, Model model,
							@RequestParam("quantity") int quantity) {		
		System.out.println("product = " + product);
		System.out.println("member = " + member);
		System.out.println("quantity = " + quantity);
		orderService.buy(product, member, quantity);
		
		return "redirect:/";
	}
	
	/** 주문 목록 */
	@RequestMapping("/orderList")
	public String getOrderList() {
		
		return "mypage/orderList";
	}

}
