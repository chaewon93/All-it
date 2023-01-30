package com.ezen.allit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.allit.domain.CustomerCenter;

public interface CustomerCenterRepository extends JpaRepository<CustomerCenter, Integer> {

	List<CustomerCenter> findCustomerCenterByCategoryContaining(String cate);
}
