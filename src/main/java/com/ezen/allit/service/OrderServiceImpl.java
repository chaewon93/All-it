package com.ezen.allit.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	
	/** 주문번호 생성 매서드 - ono를 검색해서 값이 있으면 그 값을, 없으면 새로 1을 반환하는 매서드  */
	@Transactional
	public int selectMaxOno() {
		return ordersDetailRepo.selectMaxOno();
	}
	
	/** 주문 1 - 주문번호(orders) 생성 */
	@Transactional
	public void saveOrders(Member member) {		
		ordersRepo.saveOrderSequence(member.getId());
	}
	
	/** 주문 2 - 주문상세(ordersDetail) 생성 */
	@Transactional
	public void saveOrdersDetail(Product product, Member member, OrdersDetail ordersDetail) {
		/* 주문번호 생성 매서드 사용, ono 반환 */
		int ono = selectMaxOno();

		ordersDetailRepo.saveOrder(product.getPno(), ono, member.getId(), ordersDetail.getQuantity(), ordersDetail.getFinalPrice(), ordersDetail.getReceiverName(), ordersDetail.getReceiverZipcode(), ordersDetail.getReceiverAddr(), ordersDetail.getReceiverPhone());
	}
	
	/** 주문목록 조회 1 - member에 대한 Orders 조회 */
	@Override
	public Page<Orders> getOrder(Member member, Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 10;
		
		return ordersRepo.findAllByMemberId(member.getId(), PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "ono")));
	}
	
	/** 주문목록 조회 2 - Orders에 대한 OrdersDetail 조회 */
	@Transactional
	public Page<OrdersDetail> getOrderDetail(Member member, Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 10;
		
		Page<OrdersDetail> orderList = 
				ordersDetailRepo.findAllByMemberId(member.getId(), PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "odno")));
		
		return orderList;
	}
}