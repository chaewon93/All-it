package com.ezen.allit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.allit.domain.Cart;
import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.Product;

public interface CartRepository extends JpaRepository<Cart, Integer> {
	
	// 장바구니 조회
	List<Cart> findByMember(Member member);
	
	// 장바구니 담기 전 같은 상품이 담겨있는지 확인
	Cart findByMemberAndProduct(Member member, Product product);
	
}
