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
	 * 주문번호 생성 매서드 - ono를 검색해서 값이 있으면 그 값을, 없으면 새로 1을 반환하는 매서드
	 */
	@Transactional
	public int selectMaxOno() {
		return ordersDetailRepo.selectMaxOno();
	}
	
	/*
	 * 바로구매 1 - 주문번호(orders) 생성
	 */
	@Transactional
	public void saveOrders(Member member) {		
		ordersRepo.saveOrderSequence(member.getId());
	}
	
	/*
	 * 바로구매 2 - 주문상세(ordersDetail) 생성
	 */
	@Transactional
	public void saveOrdersDetail(Product product, Member member,
								@RequestParam("quantity") int quantity) {
		/* 주문번호 생성 매서드 사용, ono 반환 */
		int ono = selectMaxOno();
		
		ordersDetailRepo.saveOrder(product.getPno(), quantity, ono, member.getId());
	}
	
		
}





