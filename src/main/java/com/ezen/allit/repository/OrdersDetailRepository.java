package com.ezen.allit.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.Orders;
import com.ezen.allit.domain.OrdersDetail;

public interface OrdersDetailRepository extends JpaRepository<OrdersDetail, Integer> {
	// 주문번호생성 매서드
	@Query(value = "SELECT NVL2(MAX(ono), MAX(ono), 1) FROM orders", nativeQuery = true)
	int selectMaxOno();
	
	// 상품주문
	@Modifying
	@Query(value = "INSERT INTO orders_detail(pno, ono, mid, quantity, receiver_name, receiver_zipcode, receiver_addr, receiver_phone, status) VALUES(?1, ?2, ?3, ?4, ?5, ?6, ?7, ?8, 1)", nativeQuery = true)
	int saveOrder(int pno, int ono, String mid, int quantity, String receiverName, String receiverZipcode, String receiverAddr, String receiverPhone);
	
	// 쿠폰사용
	@Modifying
	@Query(value = "INSERT INTO orders_detail(mcid) VALUES(?1)", nativeQuery = true)
	int saveCouponOrder(int mcid);
	
	// 사용자 주문상세 조회
	List<OrdersDetail> findByMemberAndOrders(Member member, Orders order);
	
	// 주문상태 변경
	@Modifying(clearAutomatically = true)
	@Query(value = "UPDATE orders_detail SET status = ?1 WHERE odno = ?2)", nativeQuery = true)
	int updateStatus(int status, int odno);
	
	// 판매자 주문목록조회 (검색 x)
	Page<OrdersDetail> findAllByProductSellerId(String id, Pageable pageable);
	
	// 판매자 주문목록조회 (검색 o)
	Page<OrdersDetail> findAllByProductSellerIdAndProductNameContaining(String id, String searchKeyword, Pageable pageable);


}
