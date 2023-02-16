package com.ezen.allit.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ezen.allit.domain.MemCoupon;
import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.Orders;
import com.ezen.allit.domain.OrdersDetail;
import com.ezen.allit.domain.Product;
import com.ezen.allit.repository.MemCouponRepository;
import com.ezen.allit.dto.OrdersDetailRequestDto;
import com.ezen.allit.repository.OrdersDetailRepository;
import com.ezen.allit.repository.OrdersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
	private final OrdersRepository ordersRepo;
	private final OrdersDetailRepository ordersDetailRepo;
	private final MemCouponRepository memCouRepo;
	
	/** 주문번호 생성 매서드 - ono를 검색해서 값이 있으면 그 값을, 없으면 새로 1을 반환하는 매서드  */
	@Transactional
	public int selectMaxOno() {
		return ordersDetailRepo.selectMaxOno();
	}
	
	/** 주문 1 - 주문번호(orders) 생성 */
	@Transactional
	public void saveOrders(Member member, int finalPrice, int usePoint) {		
		ordersRepo.saveOrderSequence(member.getId(), finalPrice, usePoint);
	}
	
	/** 주문 2 - 주문상세(ordersDetail) 생성 */
	@Transactional
	public void saveOrdersDetail(Product product, Member member, OrdersDetail ordersDetail) {
		/* 주문번호 생성 매서드 사용, ono 반환 */
		int ono = selectMaxOno();

		ordersDetailRepo.saveOrder(product.getPno(), ono, member.getId(), ordersDetail.getQuantity(), ordersDetail.getReceiverName(), ordersDetail.getReceiverZipcode(), ordersDetail.getReceiverAddr(), ordersDetail.getReceiverPhone());
	}
	
	/** 주문목록 조회 - member에 대한 Orders 조회 */
	@Override
	public Page<Orders> getOrder(Member member, Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 10;
		
		return ordersRepo.findAllByMemberId(member.getId(), PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "ono")));
	}
	
	/** 주문상세 조회 - Orders에 대한 OrdersDetail 조회 */
	@Transactional
	public List<OrdersDetail> getOrderDetail(Member member, Orders order) {
		
		List<OrdersDetail> orderDetailList = 
				ordersDetailRepo.findByMemberAndOrders(member, order);
		
		return orderDetailList;
	}
	
	/** 주문 상태 변경 */
	@Transactional
	@Override
	public void updateStatus(int status, int odno) {
		OrdersDetail detail = ordersDetailRepo.findById(odno).get();
		detail.setStatus(status);
		//return ordersDetailRepo.updateStatus(status, odno);
	}
	
	/** 주문 취소 - OrdersDetail 삭제 */
//	@Override
//	public void deleteOrdersDetail(int odno) {
//		ordersDetailRepo.deleteById(odno);
//	}
	
	/** 주문 취소 - Orders 삭제 */
//	@Override
//	public void deleteOrders(int ono) {
//		ordersRepo.deleteById(ono);
//	}
	
	/** 주문 취소 - Orders의 finalPrice 수정 */
	@Override
	public void updateOrders(int ono, int finalPrice) {
		Orders order = ordersRepo.findById(ono).get();
		order.setFinalPrice(finalPrice);
	}
	
	/** 오더 디테일에 사용한 쿠폰 등록 */
	@Transactional
	public void saveCouponOrder(int mcid, int couProid) {
		int ono = selectMaxOno();
		System.out.println("맥스 ono11111111");
		System.out.println(ono);
		MemCoupon memCou = memCouRepo.findById(mcid).get();
		System.out.println("맥스 ono2222222");
		Orders orders = ordersRepo.findById(ono).get();
		System.out.println("맥스 ono33333333");
		List<OrdersDetail> detailList = orders.getOrdersDetail();
		System.out.println("맥스 ono44444444");
		
		for(OrdersDetail ordersDetail : detailList) {
			int pno = ordersDetail.getProduct().getPno();
			if(pno == couProid) {
				ordersDetail.setMemCoupon(memCou);
				memCou.setStatus(1);
				System.out.println("=========================================== 최종 확인 사용된 쿠폰");
				System.out.println(memCou);
				System.out.println(ordersDetail);
				break;
			}
		}
		
	}
	
	/** 판매자 주문상태 수정 */
	@Transactional
	public void modifyOrderStatus(OrdersDetailRequestDto detailRequestDto, int status) {
		OrdersDetail ordersDetail = ordersDetailRepo.findById(detailRequestDto.getOdno()).get();
		ordersDetail.setStatus(status);
	}

}












