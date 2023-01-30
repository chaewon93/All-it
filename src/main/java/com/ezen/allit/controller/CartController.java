package com.ezen.allit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.ezen.allit.domain.Cart;
import com.ezen.allit.domain.Member;
import com.ezen.allit.service.CartService;

@Controller
@RequestMapping("/cart/")
@SessionAttributes("member")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	/** 장바구니 담기 처리 */
	@ResponseBody
	@PostMapping("/insert")
	public int insertCart(Cart cart, @ModelAttribute("member") Member member) {
		System.out.println("[session]"+member);
		if(member.getId() != null) {
			cart.setMember(member);
			cartService.insertCart(cart);
			return 1;
		} else {
			return -1;
		}
	}

}
