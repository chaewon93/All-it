package com.ezen.allit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.allit.domain.Coupon;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {

}
