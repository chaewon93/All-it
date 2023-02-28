package com.ezen.allit.service;

import java.util.List;

import com.ezen.allit.domain.Cart;
import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.Product;

public interface CartService {

	// 장바구니 담기/수량 변경
	void insertCart(Cart cart);
	
	// 장바구니 담기 전 같은 상품이 담겨있는지 확인
	Cart checkCart(Member member, Product product);
	
	// 장바구니 조회
	List<Cart> getCartList(Member member);
	
	// 장바구니에서 삭제
	void deleteCart(int cno);
}
