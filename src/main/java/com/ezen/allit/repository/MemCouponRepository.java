package com.ezen.allit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.allit.domain.Coupon;
import com.ezen.allit.domain.MemCoupon;
import com.ezen.allit.domain.Member;

public interface MemCouponRepository extends JpaRepository<MemCoupon, Integer> {

	MemCoupon findMemCouponByCouponAndMember(Coupon coupon, Member member);
}
