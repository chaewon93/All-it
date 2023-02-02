package com.ezen.allit.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRange;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.ezen.allit.domain.Cart;
import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.Product;
import com.ezen.allit.service.CartService;
import com.ezen.allit.service.ProductService;

@Controller
@RequestMapping("/cart/")
@SessionAttributes("member")
public class CartController {
	
	@Autowired
	private CartService cartService;
	@Autowired
	private ProductService prodService;
	
	@ModelAttribute("member")
	public Member setMember() {
		return new Member();
	}
	
	/** 장바구니 담기 처리 */
	@ResponseBody
	@PostMapping("/insert")
	//public int insertCart(@ModelAttribute("member") Member member, Cart cart, @RequestBody(required=false) Map<String, Object> param) {
	public String insertCart(@RequestBody Map<String, Object> param,
						@ModelAttribute("member") Member member) {
		
		//System.out.println("====[member]==== : "+member);
		//System.out.println("====[param]==== : "+param);
		
		Cart cart = new Cart();
		cart.setQuantity(Integer.parseInt(param.get("quantity").toString()));
		
		Product product = new Product();
		product.setPno(Integer.parseInt(param.get("pno").toString()));
		cart.setProduct(product);
		
		if(member.getId() != null || !member.getId().equals(null)) {
			//System.out.println("====[cart]==== : "+cart);
			cart.setMember(member);
			cartService.insertCart(cart);
			return "inCart";
		} else {
			return "noUser";
		}
	}
	
	/** 장바구니 조회 */
	@GetMapping("/cartList")
	public String getCartList(Model model,
							@ModelAttribute("member") Member member) {
		System.out.println("=============[getCartList()]===============");
		System.out.println("member : "+member);
		
		int totalPrice = 0;
		List<Product> prodList = new ArrayList<>();
		
		List<Cart> cartList = cartService.getCartList(member);
		System.out.println("cartList : "+cartList);
		
		for(int i=0; i<cartList.size(); i++) {
			Product product = prodService.getProduct(cartList.get(i).getProduct().getPno());
			totalPrice += product.getPrice() * cartList.get(i).getQuantity();
			prodList.add(product);
		}
		System.out.println("prodList : "+prodList);
		
		model.addAttribute("cartList", cartList);
		model.addAttribute("prodList", prodList);
		model.addAttribute("totalPrice", totalPrice);
		
		return "member/cartList";
	}

}
