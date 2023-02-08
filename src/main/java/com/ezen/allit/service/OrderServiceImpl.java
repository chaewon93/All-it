package com.ezen.allit.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.Orders;
import com.ezen.allit.domain.OrdersDetail;
import com.ezen.allit.domain.Product;
import com.ezen.allit.repository.OrdersDetailRepository;
import com.ezen.allit.repository.OrdersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
	private final OrdersRepository ordersRepo;
	private final OrdersDetailRepository ordersDetailRepo;

	@Override
	public List<Orders> getOrder(Orders orders) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	 * 바로구매
	 */
	@Transactional
	public int buy(Product product, Member member,
				@RequestParam("quantity") int quantity) {
		// 1. 신규 주문번호 할당
		Orders orders = ordersRepo.findById(ordersRepo.saveOrderSequence(member.getId())).get();
		
		// 2. 주문번호를 토대로 주문테이블에 저장
		OrdersDetail ordersDetail = new OrdersDetail();
		ordersDetailRepo.saveOrder(product, quantity, orders.getOno(), member.getId());
		
		// 3. 상품페이지에서 상품정보 읽어서 주문상세테이블에 저장
		return orders.getOno();
	}
}
