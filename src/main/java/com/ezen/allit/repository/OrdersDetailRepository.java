package com.ezen.allit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ezen.allit.domain.OrdersDetail;
import com.ezen.allit.domain.Product;

public interface OrdersDetailRepository extends JpaRepository<OrdersDetail, Integer> {
	
	@Modifying
	@Query(value =  "INSERT INTO orders_detail(product, quantity, ono, mid) VALUES(?1, ?2, ?3, ?4)", nativeQuery = true)
	int saveOrder(Product product, int quantity, int ono, String mid);
}
