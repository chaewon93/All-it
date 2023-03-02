package com.ezen.allit.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ezen.allit.domain.Product;
import com.ezen.allit.domain.Role;
import com.ezen.allit.dto.Chart;

public interface ProductRepository extends JpaRepository<Product, Integer> {
	// 상품목록조회 (검색조건 x, 검색어 x)
	Page<Product> findAllByStatusAndSellerRole(int status, Role role, Pageable pageable);

	// 신상품목록조회
//	@Query(value = "SELECT p.pno, p.category, p.content, p.count, p.price, p.discount, p.first_price, p.hit, p.image_name, p.md_pickyn, p.name, p.rating, p.reg_date, p.status, p.sid"
//				 + "  FROM product p, seller s"
//				 + " WHERE p.sid = s.id"
//				 + "   AND TO_CHAR(sysdate, 'YYYYMMDD')-TO_CHAR(p.reg_date, 'YYYYMMDD')<8"
//				 + "   AND p.status = 1"
//				 + "   AND s.role = 'SELLER'", nativeQuery = true)
	@Query(value="SELECT * FROM newproduct where sysdate - TO_DATE(TO_CHAR(reg_date, 'YYYYMMDD'), 'YYYYMMDD') < 8 AND status = 1 AND role = 'SELLER'", nativeQuery = true)	
	Page<Product> getNewProductList(Pageable pageable);
	
	// 베스트상품목록조회
//	@Query(value=" SELECT p.pno, p.category, p.content, p.count, p.price, p.discount, p.first_price, p.hit, p.image_name, p.md_pickyn, p.name, p.rating, p.reg_date, p.status, p.sid"
//			+ "     FROM product p"
//			+ "     INNER JOIN seller s"
//			+ "     ON p.sid = s.id"
//			+ "     WHERE ROWNUM >= 1 AND ROWNUM <= 18 AND p.status = 1 AND s.role = 'SELLER' ORDER BY p.hit DESC", nativeQuery = true)
	@Query(value="SELECT * FROM bestproduct WHERE ROWNUM >= 1 AND ROWNUM <= 18 AND status = 1 AND role = 'SELLER' ORDER BY hit DESC", nativeQuery = true)
	Page<Product> getBestProductList(Pageable pageable);
	
	// 특가세일상품목록조회
	Page<Product> findAllByDiscountNotAndStatusAndSellerRole(int discount, int status, Role role, Pageable pageable);

	// 상품검색 (검색조건 x, 검색어 o)
	Page<Product> findAllByNameContainingAndStatusAndSellerRole(String searchKeyword, int status, Role role, Pageable pageable);
	
	// 상품검색 (검색조건 o, 검색어 x), 카테고리별 상품조회 
	Page<Product> findAllByCategoryAndStatusAndSellerRole(int searchCondition, int status, Role role, Pageable pageable);

	// 상품검색 (검색조건 o, 검색어 o)
	Page<Product> findAllByCategoryAndNameContainingAndStatusAndSellerRole(int searchCondition, String searchKeyword, int status, Role role, Pageable pageable);

	Page<Product> findProductByStatus(int status, Pageable pageable);

	Page<Product> findByStatusAndNameContaining(int status, String searchKeyword, Pageable pageable);
	
	// mdPick 목록
	Page<Product> findProductBymdPickynAndStatus(int y, Pageable pageable, int status);
	
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
		
	// 차트에 필요한 상품 쿼리
	@Query(value="select category, COUNT(*) as count from product group by category ORDER BY category ASC", nativeQuery = true)
	List<Chart> chartCategoryGroup();

}
