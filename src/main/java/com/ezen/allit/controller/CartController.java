package com.ezen.allit.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
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
			
			Cart cartList = cartService.checkCart(member, product);
			if(cartList != null && cartList.getProduct().getPno() == product.getPno()) {
				return "exist";
			} else {
				cartService.insertCart(cart);
				return "inCart";
			}
		} else {
			return "noUser";
		}
	}
	
	/** 장바구니 조회 처리 */
	@GetMapping("/cartList")
	public String getCartList(Model model,
							@ModelAttribute("member") Member member) {
		System.out.println("=============[getCartList()]===============");
		System.out.println("member : "+member);
		
		int totalPrice = 0;
		List<Cart> cartList = cartService.getCartList(member);
		System.out.println("cartList : "+cartList);
		
		for(int i=0; i<cartList.size(); i++) {
			totalPrice += cartList.get(i).getProduct().getPrice() * cartList.get(i).getQuantity();
		}
		
		model.addAttribute("cartList", cartList);
		model.addAttribute("totalPrice", totalPrice);
		
		return "member/cartList";
	}
	
	/** 장바구니에서 삭제 처리 */
	@ResponseBody
	@PostMapping("/delCart")
	public void deleteCart(Cart cart, HttpServletRequest request) {
		String[] ajaxParam = request.getParameterValues("cnoArr");
		System.out.println("[delCart cnt] : "+ajaxParam.length);
		
		try {
			for(int i=0; i<ajaxParam.length; i++) {
				System.out.println(ajaxParam[i]);
				cartService.deleteCart(Integer.valueOf(ajaxParam[i]));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** 장바구니 수량 변경 처리 */
	@ResponseBody
	@PostMapping("/modCart")
	public String modifyCart(@RequestBody Map<String, Object> param,
						@ModelAttribute("member") Member member) {
		//System.out.println("=============[modifyCart()]===============");
		Cart cart = new Cart();
		Product product = new Product();
		product.setPno(Integer.parseInt(param.get("pno").toString()));
		cart.setProduct(product);
		cart.setMember(member);
		
		if(member.getId() != null || !member.getId().equals(null)) {
			// 장바구니에 담겨있는 상품정보 조회
			Cart cartList = cartService.checkCart(member, product);
			
			if(cartList != null) {
				//System.out.println("[checkCart quantity] " + cartList.getQuantity());
				//System.out.println("[modifyCart quantity] " + param.get("quantity").toString());
				
				// update할 장바구니 번호(cno) 저장
				cart.setCno(cartList.getCno());
				
				/*
				 * 상품 상세페이지에서 같은 상품을 또 담을 경우(type : add)
				 * Cart 테이블의 quantity 기존 수량 + 변경 수량 으로 update
				 * 장바구니 목록에서 상품 수량 변경하는 경우(type : mod)
				 * Cart 테이블의 quantity 변경 수량 으로 update
				 */
				if(param.get("type").toString().equals("add")) {
					cart.setQuantity(cartList.getQuantity() + Integer.parseInt(param.get("quantity").toString()));
				} else if(param.get("type").toString().equals("mod")) {
					cart.setQuantity(Integer.parseInt(param.get("quantity").toString()));
				}
				
				//System.out.println("[Cart]"+cart);
				cartService.insertCart(cart);
				return "modCart";
			} else {
				return "null";
			}
		} else {
			return "noUser";
		}
	}

}
