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
import com.ezen.allit.repository.MemberRepository;
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
	private final MemberRepository memberRepo;
	private final OrderService orderService;
	
	@ModelAttribute("user")
	public Member setMember() {
		return new Member();
	}
	
	/** 즉시구매 시 주문/결제 페이지 요청 */
	@PostMapping("/orderNow")
	public String getOrderView(Product product, Model model,
							@RequestParam("quantity") int quantity) {
		
		Product theProduct = productRepo.findById(product.getPno()).get();
		model.addAttribute("product", theProduct);
		model.addAttribute("quantity", quantity);
		
		return "mypage/orderInfo";
	}
	
	/** 장바구니에서 주문/결제 페이지 요청 */
	@PostMapping("/orderInfo")
	public String ordersView(Cart cart) {
		
		return "mypage/orderInfo";
	}
	
	/** 주문하기 */
	@PostMapping("/orders")
	public String insertOrder(int pno, String mid, Model model,
							@RequestParam("quantity") int quantity) {	
		
		Product product = productRepo.findById(pno).get();
		Member member = memberRepo.findById(mid).get(); 

		orderService.saveOrders(member);
		orderService.saveOrdersDetail(product, member, quantity);
		
		return "redirect:/";
	}
	
	/** 주문 목록 */
	@RequestMapping("/orderList")
	public String getOrderList() {
		
		return "mypage/orderList";
	}

}
