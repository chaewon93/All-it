package com.ezen.allit.service;

import com.ezen.allit.domain.Coupon;
import com.ezen.allit.domain.Member;

public interface CouponService {

	void createCoupon(Coupon coupon);
	
	void insertMemCoupon(Member member, int couid);
}
