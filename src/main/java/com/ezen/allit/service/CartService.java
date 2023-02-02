package com.ezen.allit.service;

import java.util.List;

import com.ezen.allit.domain.Cart;
import com.ezen.allit.domain.Member;

public interface CartService {

	// 장바구니 담기
	void insertCart(Cart cart);
	
	// 장바구니 조회
	List<Cart> getCartList(Member member);
}
