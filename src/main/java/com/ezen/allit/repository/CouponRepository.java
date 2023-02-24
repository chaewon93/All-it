package com.ezen.allit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.allit.domain.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {

	// 회원이 받을 수 있는 쿠폰 검색(등급-회원등급과 ALL or 성별-남자,여자와 남녀)
	List<Coupon> findCouponByConditionContainingOrConditionContaining(String condition, String condition1);
	
	// MDPICK 조건 쿠폰 검색
	List<Coupon> findCouponByConditionContaining(String condition);
}
