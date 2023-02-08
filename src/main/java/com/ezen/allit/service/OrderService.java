package com.ezen.allit.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestParam;

import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.Orders;
import com.ezen.allit.domain.Product;

public interface OrderService {
	
	// 주문 조회
	List<Orders> getOrder(Orders orders);

	int buy(Product product, Member member, @RequestParam("quantity") int quantity);
}
