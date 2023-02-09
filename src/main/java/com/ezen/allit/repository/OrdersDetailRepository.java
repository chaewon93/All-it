package com.ezen.allit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ezen.allit.domain.OrdersDetail;
import com.ezen.allit.domain.Product;

public interface OrdersDetailRepository extends JpaRepository<OrdersDetail, Integer> {
	@Query(value = "SELECT NVL2(MAX(ono), MAX(ono), 1) FROM orders", nativeQuery = true)
	int selectMaxOno();
	
	// 상품주문
	@Modifying
	@Query(value =  "INSERT INTO orders_detail(pno, ono, mid, quantity, receiver_name, receiver_zipcode, receiver_addr, receiver_phone, status) VALUES(?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, 1)", nativeQuery = true)
	int saveOrder(int pno, int ono, String mid, int quantity, String receiverName, String receiverZipcode, String receiverAddr, String receiverPhone);
}
