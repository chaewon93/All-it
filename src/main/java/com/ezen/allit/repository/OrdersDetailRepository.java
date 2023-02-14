package com.ezen.allit.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.Orders;
import com.ezen.allit.domain.OrdersDetail;

public interface OrdersDetailRepository extends JpaRepository<OrdersDetail, Integer> {
	// 주문번호생성 매서드
	@Query(value = "SELECT NVL2(MAX(ono), MAX(ono), 1) FROM orders", nativeQuery = true)
	int selectMaxOno();
	
	// 상품주문
	@Modifying
	@Query(value =  "INSERT INTO orders_detail(pno, ono, mid, quantity, final_price, receiver_name, receiver_zipcode, receiver_addr, receiver_phone, status) VALUES(?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, ?9, 1)", nativeQuery = true)
	int saveOrder(int pno, int ono, String mid, int quantity, int finalPrice, String receiverName, String receiverZipcode, String receiverAddr, String receiverPhone);
	
	// 사용자 주문상세 조회
	List<OrdersDetail> findByMemberAndOrders(Member member, Orders order);
	
	// 판매자 주문목록조회
	Page<OrdersDetail> findAllByProductSellerId(String id, Pageable pageable);

}
