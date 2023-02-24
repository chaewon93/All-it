package com.ezen.allit.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

import com.ezen.allit.config.auth.PrincipalDetailMember;
import com.ezen.allit.domain.Cart;
import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.Product;
import com.ezen.allit.service.CartService;
import com.ezen.allit.service.CouponService;

@Controller
@RequestMapping("/cart")
@SessionAttributes("user")
public class CartController {
	
	@Autowired
	private CartService cartService;
	
	@Autowired
	private CouponService couponService;
	
	@ModelAttribute("user")
	public Member setMember(@AuthenticationPrincipal PrincipalDetailMember principal) {
		if(principal != null) {
			Member member = principal.getMember();
			System.out.println("member=============== = " + member);
			return member;
		} else {
			return null;
		}
	}
	
	/** 장바구니 담기 처리
	 * @author 임채원
	 * @param param		장바구니 페이지에서 ajax로 넘긴 데이터(상품번호, 수량)
	 * @param principal	로그인시 저장된 사용자 정보
	 * @return			"exist":이미 장바구니에 같은 상품이 담겨 있음 / "inCart":상품을 처음 장바구니에 담음 / "noUser":로그인 정보가 없음
	 */
	@ResponseBody
	@PostMapping("/insert")
	//public int insertCart(@ModelAttribute("member") Member member, Cart cart, @RequestBody(required=false) Map<String, Object> param) {
	public String insertCart(@RequestBody Map<String, Object> param,
							@AuthenticationPrincipal PrincipalDetailMember principal) {
		
		//System.out.println("====[member]==== : "+member);
		//System.out.println("====[param]==== : "+param);
		
		Cart cart = new Cart();
		cart.setQuantity(Integer.parseInt(param.get("quantity").toString()));
		
		Product product = new Product();
		product.setPno(Integer.parseInt(param.get("pno").toString()));
		cart.setProduct(product);
		
		if(principal.getMember().getId() != null || !principal.getMember().getId().equals(null)) {
			//System.out.println("====[cart]==== : "+cart);
			cart.setMember(principal.getMember());
			
			Cart cartList = cartService.checkCart(principal.getMember(), product);
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
	
	/** 장바구니 조회 처리 
	 * @author 임채원
	 * @param model		장바구니 페이지에 넘겨줄 정보(장바구니 목록, 총 상품가격) 저장 시 사용
	 * @param principal	로그인시 저장된 사용자 정보
	 * @return
	 */
	@GetMapping("/cartList")
	public String getCartList(Model model,
							@AuthenticationPrincipal PrincipalDetailMember principal) {
		//System.out.println("=============[getCartList()]===============");
		System.out.println("member : "+principal.getMember());
		
		int totalPrice = 0;
		List<Cart> cartList = cartService.getCartList(principal.getMember());
		//System.out.println("cartList : "+cartList);
		
		for(int i=0; i<cartList.size(); i++) {
			totalPrice += cartList.get(i).getProduct().getPrice() * cartList.get(i).getQuantity();
		}

		model.addAttribute("cartList", cartList);
		model.addAttribute("totalPrice", totalPrice);
		
		return "mypage/cartList";
	}
	
	/** 장바구니에서 삭제 처리
	 * @author 임채원
	 * @param request	장바구니 페이지에서 ajax로 넘긴 데이터(삭제할 장바구니 번호 배열)를 사용하기 위한 객체
	 */
	@ResponseBody
	@PostMapping("/delCart")
	public void deleteCart(HttpServletRequest request) {
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
	
	/** 장바구니 수량 변경 처리
	 * @author 임채원
	 * @param param		장바구니 페이지에서 ajax로 넘긴 데이터(상품번호, 장바구니 수량변경 타입)
	 * @param principal	로그인시 저장된 사용자 정보
	 * @return			"modCart":장바구니 수량변경 성공 / "null":장바구니에 담긴 상품정보 없음 또는 오류 / "noUser":로그인 정보가 없음
	 */
	@ResponseBody
	@PostMapping("/modCart")
	public String modifyCart(@RequestBody Map<String, Object> param,
							@AuthenticationPrincipal PrincipalDetailMember principal) {
		//System.out.println("=============[modifyCart()]===============");
		Cart cart = new Cart();
		Product product = new Product();
		product.setPno(Integer.parseInt(param.get("pno").toString()));
		cart.setProduct(product);
		cart.setMember(principal.getMember());
		
		if(principal.getMember().getId() != null || !principal.getMember().getId().equals(null)) {
			// 장바구니에 담겨있는 상품정보 조회
			Cart cartList = cartService.checkCart(principal.getMember(), product);
			
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
	
	/**
	 * @author 박현일
	 * @param map
	 * @return
	 */
	@PostMapping("useCoupon")
	@ResponseBody
	public Map<String, Integer> useCoupon(@RequestParam Map<String,Object> map) {
		System.out.println("====================================== useCopon1");
		System.out.println(map);
		int memCouid = Integer.parseInt(String.valueOf(map.get("memCouid")));
		int totp = Integer.parseInt(String.valueOf(map.get("totp")));
		System.out.println("====================================== useCopon2");
		System.out.println(map);
		int price = couponService.checkPrice(memCouid, totp);
		System.out.println(price);
		System.out.println("====================================== useCopon3");
		Map<String, Integer> map1 = new HashMap<>();
		map1.put("price", price);
		return map1;
	}
	
	/**
	 * @author 박현일
	 * @param member
	 * @param map
	 * @return
	 */
	@PostMapping("useCoupon1")
	@ResponseBody
	public Map<String, Integer> useCoupon1(@ModelAttribute("user") Member member,
				@RequestParam Map<String,Object> map) {
		System.out.println("====================================== useCopon1");
		System.out.println(map);
		int memCouid = Integer.parseInt(String.valueOf(map.get("memCouid")));
		int price = Integer.parseInt(String.valueOf(map.get("price")));
		System.out.println("====================================== useCopon2");
		System.out.println(map);
		int fprice = couponService.checkPrice(memCouid, price);
		System.out.println(fprice);
		System.out.println("====================================== useCopon3");
		Map<String, Integer> map1 = new HashMap<>();
		map1.put("fprice", fprice);
		map1.put("memCouid", memCouid);
		return map1;
	}

}
