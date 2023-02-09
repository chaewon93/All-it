package com.ezen.allit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ezen.allit.domain.OrdersDetail;
import com.ezen.allit.domain.Product;

public interface OrdersDetailRepository extends JpaRepository<OrdersDetail, Integer> {
	@Query(value = "SELECT NVL2(MAX(ono), MAX(ono), 1) FROM orders", nativeQuery = true)
	int selectMaxOno();
	
	@Modifying
	@Query(value =  "INSERT INTO orders_detail(pno, quantity, ono, mid, status) VALUES(?1, ?2, ?3, ?4, 1)", nativeQuery = true)
	int saveOrder(int pno, int quantity, int ono, String mid);
}
