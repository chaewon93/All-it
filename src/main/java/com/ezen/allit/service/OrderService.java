package com.ezen.allit.service;

import java.util.List;

import com.ezen.allit.domain.Orders;

public interface OrderService {
	
	// 주문 조회
	List<Orders> getOrder(Orders orders);

}
