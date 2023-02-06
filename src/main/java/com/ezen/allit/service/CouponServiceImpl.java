package com.ezen.allit.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.allit.domain.Coupon;
import com.ezen.allit.domain.MemCoupon;
import com.ezen.allit.domain.Member;
import com.ezen.allit.repository.CouponRepository;
import com.ezen.allit.repository.MemCouponRepository;
import com.ezen.allit.repository.MemberRepository;

@Service
public class CouponServiceImpl implements CouponService {

	@Autowired
	CouponRepository couponRepo;
	
	@Autowired
	MemberRepository memberRepo;
	
	@Autowired
	MemCouponRepository memCouRepo;
	
	@Override
	public void createCoupon(Coupon coupon) {
		
        Random random = new Random();
        int length = 12;

        StringBuffer word = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int choice = random.nextInt(3);
            switch(choice) {
                case 0:
                	word.append((char)((int)random.nextInt(25)+97));
                    break;
                case 1:
                	word.append((char)((int)random.nextInt(25)+65));
                    break;
                case 2:
                	word.append((char)((int)random.nextInt(10)+48));
                    break;
                default:
                    break;
            }
        }
        System.out.println("word = (" + word + "), length = " + length);
        
        coupon.setCouName(word.toString());
		
        System.out.println("---------------------------");
        System.out.println(coupon);
        System.out.println("---------------------------");
        
        couponRepo.save(coupon);
	}

	@Override
	public void insertMemCoupon(Member member, int couid) {
		
		Coupon coupon = couponRepo.findById(couid).get();
		
		MemCoupon memCoupon = new MemCoupon();
		memCoupon.setMember(member);
		memCoupon.setCoupon(coupon);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(memCoupon.getCreateMemCouDate());
		cal.add(Calendar.DATE, coupon.getPeriod());
		memCoupon.setEndMemCouDate(cal.getTime());;

		List<MemCoupon> memCouList = new ArrayList<>();
		memCouList.add(memCoupon);
		
//		coupon.setMemCoupon(memCouList);
		
		memCouRepo.save(memCoupon);
		
		member.setMemCoupon(memCouList);
		coupon.setMemCoupon(memCouList);
		
		memberRepo.save(member);
		couponRepo.save(coupon);
	}
}
