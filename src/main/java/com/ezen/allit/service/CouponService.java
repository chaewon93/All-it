package com.ezen.allit.service;

import java.util.List;

import com.ezen.allit.domain.Coupon;
import com.ezen.allit.domain.MemCoupon;
import com.ezen.allit.domain.Member;

public interface CouponService {

	void createCoupon(Coupon coupon);
	
	void insertMemCoupon(Member member, int couid);
	
	List<Coupon> forMemberCouponList(Member member, int pno); 
	
	List<MemCoupon> MemProCouponList(Member member, int pno);
	
	int checkPrice(int memCouid, int pno);
}
