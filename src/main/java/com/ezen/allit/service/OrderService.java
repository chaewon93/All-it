package com.ezen.allit.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.Orders;
import com.ezen.allit.domain.OrdersDetail;
import com.ezen.allit.domain.Product;

public interface OrderService {
	
	void saveOrders(Member member, int finalPrice, int usePoint);
	
	void saveOrdersDetail(Product product, Member member, OrdersDetail orderDetail);
	
	void saveCouponOrder(int mcid, int couProid);

	/** 주문목록 조회(주문 조회) */
	Page<Orders> getOrder(Member member, Pageable pageable);

	/** 주문상세 조회 */
	List<OrdersDetail> getOrderDetail(Member member, Orders order);
	
	/** 주문 상태 변경 */
	void updateStatus(int status, int odno);
	
	/** 주문 취소 - OrdersDetail 삭제 */
//	void deleteOrdersDetail(int odno);
	
	/** 주문 취소 - Orders 삭제 */
//	void deleteOrders(int ono);
	
	/** 주문 취소 - 
	
	/** 주문 취소 - Orders의 finalPrice 수정 */
	void updateOrders(int ono, int finalPrice);

}
