package com.ezen.allit.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ezen.allit.domain.Coupon;
import com.ezen.allit.domain.MemCoupon;
import com.ezen.allit.domain.Member;

public interface CouponService {

	void createCoupon(Coupon coupon);
	
	void updateCoupon(Coupon coupon);
	
	void insertMemCoupon(Member member, int couid);
	
	List<Coupon> forMemberCouponList(Member member, int pno); 
	
	List<MemCoupon> MemProCouponList(Member member, int pno);
	
	int checkPrice(int memCouid, int pno);
	
	Page<Coupon> findCouponList(Pageable pageable);

	/** 취소/반품시 쿠폰 복원 */
	void updateStatus(int memCouid, int status);
}
