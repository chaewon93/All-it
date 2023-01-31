package com.ezen.allit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ezen.allit.domain.Product;
import com.ezen.allit.domain.Role;
import com.ezen.allit.domain.Seller;

public interface SellerRepository extends JpaRepository<Seller, String> {

	// 판매자 로그인
	Seller findByIdAndPwd(String id, String pwd);
	
	Optional<Seller> findById(String id);
	
	// 판매자별 검색 후 조회
//	Page<Seller> findByNameContaining(String id, String searchKeyword, PageRequest pageRequest);
	
	// 판매자별 상품 조회
//	Page<Seller> findAllById(String id, PageRequest pageRequest);
	
	// 등록대기 판매자, 등록완료 판매자 조회
	List<Seller> findSellerByRoleNot(Role role);
	
	// 관리자 조회
	List<Seller> findSellerByRole(Role role);
}
