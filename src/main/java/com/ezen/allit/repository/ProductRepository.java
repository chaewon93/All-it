package com.ezen.allit.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.allit.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	// 상품검색
	Page<Product> findByNameContaining(String searchKeyword, PageRequest pageRequest);
	
	Page<Product> findProductByStatus(int status, Pageable pageable);
}
