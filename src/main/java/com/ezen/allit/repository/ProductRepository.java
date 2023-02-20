package com.ezen.allit.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ezen.allit.domain.Product;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	// 상품목록조회 (검색조건 x, 검색어 x)
	Page<Product> findAllByStatus(int status, Pageable pageable);

	// 신상품목록조회
	@Query(value = "SELECT * FROM product WHERE TO_CHAR(sysdate, 'YYYYMMDD')-TO_CHAR(reg_date, 'YYYYMMDD')<8 AND status = 1", nativeQuery = true)
	Page<Product> getNewProductList(Pageable pageable);

	// 상품검색 (검색조건 x, 검색어 o)
	Page<Product> findAllByNameContainingAndStatus(String searchKeyword, int status, Pageable pageable);
	
	// 상품검색 (검색조건 o, 검색어 x), 카테고리별 상품조회 
	Page<Product> findAllByCategoryAndStatus(int searchCondition, int status, Pageable pageable);

	// 상품검색 (검색조건 o, 검색어 o)
	Page<Product> findAllByCategoryAndNameContainingAndStatus(int searchCondition, String searchKeyword, int status, Pageable pageable);
	
	Page<Product> findProductByStatus(int status, Pageable pageable);

	Page<Product> findByStatusAndNameContaining(int status, String searchKeyword, Pageable pageable);
	
	Page<Product> findProductBymdPickyn(int y, Pageable pageable);
	
	// 메인화면에 나오는 mdPick 목록
	List<Product> findFirst3BymdPickynOrderByRegDateDesc(int y);

	// 판매자별 상품목록조회
	Page<Product> findAllBySellerIdAndStatus(String id, int status, Pageable pageable);

	// 판매자별 상품검색 (검색조선 x, 검색어 o)
	Page<Product> findAllBySellerIdAndNameContainingAndStatus(String id, String searchKeyword, int status, Pageable pageable);

	// 판매자별 상품검색 (검색조선 o, 검색어 x)
	Page<Product> findAllBySellerIdAndCategoryAndStatus(String id, int searchCondition, int status, Pageable pageable);

	// 판매자별 상품검색 (검색조선 o, 검색어 o)
	Page<Product> findAllBySellerIdAndCategoryAndNameContainingAndStatus(String id, int searchCondition, String searchKeyword, int status, Pageable pageable);

}
