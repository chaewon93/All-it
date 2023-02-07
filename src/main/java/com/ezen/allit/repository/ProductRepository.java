package com.ezen.allit.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.allit.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	
	// 상품검색
	Page<Product> findByNameContaining(String searchKeyword, Pageable pageable);
	
	Page<Product> findProductByStatus(int status, Pageable pageable);

	Page<Product> findByStatusAndNameContaining(int status, String searchKeyword, Pageable pageable);

	// 판매자별 상품검색
	Page<Product> findBySellerIdAndNameContaining(String id, String searchKeyword, Pageable pageable);
	
	// 판매자별 상품조회
	Page<Product> findAllBySellerId(String id, Pageable pageable);
	
	// 판매자별 상품 슬라이싱
	Slice<Product> findBySellerOrderByPnoDesc(String id, Pageable pageable);
}
