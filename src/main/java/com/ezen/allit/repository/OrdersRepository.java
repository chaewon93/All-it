package com.ezen.allit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ezen.allit.domain.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {

	@Modifying
	@Query(value =  "INSERT INTO orders(mid, reg_date) VALUES(?1, sysdate)", nativeQuery = true)
	int saveOrderSequence(String mid);
	
	@Modifying
	@Query(value =  "SELECT ono FROM orders WHERE ", nativeQuery = true)
	int findOno();
}
