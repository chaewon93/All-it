package com.ezen.allit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.allit.domain.Cart;
import com.ezen.allit.domain.Member;

public interface CartRepository extends JpaRepository<Cart, Integer> {
	
	// 장바구니 조회
	List<Cart> findByMember(Member member);
	
}
