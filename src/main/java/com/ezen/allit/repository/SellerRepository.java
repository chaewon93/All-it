package com.ezen.allit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.allit.domain.Seller;

public interface SellerRepository extends JpaRepository<Seller, String> {

	// 판매자 로그인
	Seller findByIdAndPwd(String id, String pwd);

}
