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

import com.ezen.allit.domain.Cart;
import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.Product;
import com.ezen.allit.service.CartService;

@Controller
@RequestMapping("/cart/")
@SessionAttributes("member")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	@ModelAttribute("member")
	public Member setMember() {
		return new Member();
	}
	
	/** 장바구니 담기 처리 */
	@ResponseBody
	@PostMapping("/insert")
	//public int insertCart(@ModelAttribute("member") Member member, Cart cart, @RequestParam("mid") String mid) {
	public int insertCart(Cart cart, Product product,
						@ModelAttribute("member") Member member) {
		
		System.out.println("pno : "+product.getPno());
		if(member.getId() == null || member.getId().equals(null)) {
			return -1;
		} else {
			cart.setMember(member);
			cartService.insertCart(cart);
			return 1;
		}
	}
	
	/** 장바구니 조회 */
	@GetMapping("/cartList")
	public String getCartList(Cart cart, Model model) {
		System.out.println("=============[cart.productList()]===============");
		System.out.println("productList size() : "+cart.getProduct());
		
		//List<Cart> cartList = cartService.getCartList(cart);
		//model.addAttribute("cartList", cartList);
		return "member/cartList";
	}

}
