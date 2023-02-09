package com.ezen.allit.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ezen.allit.domain.OrdersDetail;
import com.ezen.allit.domain.Product;

public interface OrdersDetailRepository extends JpaRepository<OrdersDetail, Integer> {
	// 주문번호생성 매서드
	@Query(value = "SELECT NVL2(MAX(ono), MAX(ono), 1) FROM orders", nativeQuery = true)
	int selectMaxOno();
	
	// 상품주문
	@Modifying
	@Query(value =  "INSERT INTO orders_detail(pno, ono, mid, quantity, receiver_name, receiver_zipcode, receiver_addr, receiver_phone, status) VALUES(?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, 1)", nativeQuery = true)
	int saveOrder(int pno, int ono, String mid, int quantity, String receiverName, String receiverZipcode, String receiverAddr, String receiverPhone);
	
	// 사용자 주문목록조회
	Page<OrdersDetail> findAllByMemberId(String id, Pageable pageable);

}
