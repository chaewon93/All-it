package com.ezen.allit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.allit.domain.Orders;

public interface OrdersRepository extends JpaRepository<Orders, Integer> {

}
