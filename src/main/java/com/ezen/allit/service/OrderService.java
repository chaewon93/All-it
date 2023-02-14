package com.ezen.allit.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestParam;

import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.Orders;
import com.ezen.allit.domain.OrdersDetail;
import com.ezen.allit.domain.Product;

public interface OrderService {
	
	void saveOrders(Member member);
	
	void saveOrdersDetail(Product product, Member member, OrdersDetail orderDetail);
	
	void saveCouponOrder(int mcid, int couProid);
	
	/** 주문 조회 */
	Page<Orders> getOrder(Member member, Pageable pageable);

	/** 주문 상세 조회 */
	Page<OrdersDetail> getOrderDetail(Member member, Pageable pageable);

}
