package com.ezen.allit.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ezen.allit.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	// 신상품목록조회
	@Query(value = "SELECT * FROM product WHERE TO_CHAR(sysdate, 'YYYYMMDD')-TO_CHAR(reg_date, 'YYYYMMDD')<8", nativeQuery = true)
	Page<Product> getNewProductList(Pageable pageable);

	// 상품검색 (검색조건 x, 검색어 o)
	Page<Product> findAllByNameContaining(String searchKeyword, Pageable pageable);
	
	// 상품검색 (검색조건 o, 검색어 o)
	Page<Product> findAllByCategoryAndNameContaining(int searchCondition, String searchKeyword, Pageable pageable);
	
	// 상품검색 (검색조건 o, 검색어 x), 카테고리별 상품조회 
	Page<Product> findAllByCategory(int searchCondition, Pageable pageable);
	
	Page<Product> findProductByStatus(int status, Pageable pageable);

	Page<Product> findByStatusAndNameContaining(int status, String searchKeyword, Pageable pageable);

	// 판매자별 상품검색
	Page<Product> findAllBySellerIdAndNameContaining(String id, String searchKeyword, Pageable pageable);
	
	// 판매자별 상품조회
	Page<Product> findAllBySellerId(String id, Pageable pageable);
	
	// 판매자별 상품 슬라이싱
	Slice<Product> findBySellerOrderByPnoDesc(String id, Pageable pageable);
	

}
