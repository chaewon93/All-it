package com.ezen.allit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.allit.domain.Role;
import com.ezen.allit.domain.Seller;

public interface SellerRepository extends JpaRepository<Seller, String> {

	// 판매자 로그인
	Seller findByIdAndPwd(String id, String pwd);
	
	// 등록대기 판매자, 등록완료 판매자 조회
	List<Seller> findSellerByRoleNot(Role role);
	
	// 관리자 조회
	List<Seller> findSellerByRole(Role role);
}
