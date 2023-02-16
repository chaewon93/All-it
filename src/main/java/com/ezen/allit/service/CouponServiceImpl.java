package com.ezen.allit.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ezen.allit.domain.Coupon;
import com.ezen.allit.domain.CustomerCenter;
import com.ezen.allit.domain.MemCoupon;
import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.Product;
import com.ezen.allit.repository.CouponRepository;
import com.ezen.allit.repository.MemCouponRepository;
import com.ezen.allit.repository.MemberRepository;

@Transactional
@Service
public class CouponServiceImpl implements CouponService {

	@Autowired
	CouponRepository couponRepo;
	
	@Autowired
	MemberRepository memberRepo;
	
	@Autowired
	MemCouponRepository memCouRepo;

	@Autowired
	ProductService proService;
	
	@Override
	public Page<Coupon> findCouponList(Pageable pageable) {
		
		int page = pageable.getPageNumber() - 1;
		int pageSize = 6;
		
		Page<Coupon> couponList = 
				couponRepo.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "couId")));

        return couponList;	
	}
	
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

		List<MemCoupon> memCouList = member.getMemCoupon();
		memCouList.add(memCoupon);
		
//		coupon.setMemCoupon(memCouList);
		
		memCouRepo.save(memCoupon);
		
		member.setMemCoupon(memCouList);
		coupon.setMemCoupon(memCouList);
		
		memberRepo.save(member);
		couponRepo.save(coupon);
	}

	@Override
	public List<Coupon> forMemberCouponList(Member member, int pno) {

		if(pno == 0) {
			List<Coupon> allCouList = couponRepo.findCouponByConditionContainingOrConditionContaining(member.getGrade().toString(), "ALL");
			
			List<Coupon> allCouList1 = couponRepo.findCouponByConditionContainingOrConditionContaining(member.getGender(), "남녀");
			
			allCouList.retainAll(allCouList1);
			return allCouList;
		}else {
			List<Coupon> allCouList = couponRepo.findCouponByConditionContainingOrConditionContaining(member.getGrade().toString(), "ALL");
			
			List<Coupon> allCouList1 = couponRepo.findCouponByConditionContainingOrConditionContaining(member.getGender(), "남녀");
			allCouList.retainAll(allCouList1);
			
			Product pro = proService.getProduct(pno);			
			List<Coupon> allCouList2 = couponRepo.findCouponByConditionContainingOrConditionContaining(Integer.toString(pro.getCategory()), "0");
			allCouList.retainAll(allCouList2);
			
			if(pro.getMdPickyn() == 0) {
				List<Coupon> allCouList3 = couponRepo.findCouponByConditionContaining("NO");
				allCouList.retainAll(allCouList3);
			}else if(pro.getMdPickyn() == 1) {
				List<Coupon> allCouList3 = couponRepo.findCouponByConditionContaining("YES");
				allCouList.retainAll(allCouList3);
			}
			
			List<Coupon> allCouList4 = couponRepo.findCouponByConditionContainingOrConditionContaining(pro.getSeller().getId(), "SELLERS");
			allCouList.retainAll(allCouList4);
			return allCouList;
		}

	}
		
	@Override
	public List<MemCoupon> MemProCouponList(Member member, int pno) {
		List<MemCoupon> list = member.getMemCoupon();
		Product pro = proService.getProduct(pno);
		List<MemCoupon> memCouList = new ArrayList<>();
		for(MemCoupon memCoupon : list) {
			if(memCoupon.getCoupon().getCondition().contains(Integer.toString(pro.getCategory())) || memCoupon.getCoupon().getCondition().contains("0")){
				if(memCoupon.getCoupon().getCondition().contains(Integer.toString(pro.getMdPickyn())) || memCoupon.getCoupon().getCondition().contains("NO")) {
					if(memCoupon.getCoupon().getCondition().contains(pro.getSeller().getId()) || memCoupon.getCoupon().getCondition().contains("SELLERS")) {
						memCouList.add(memCoupon);
					}
				}
			}
		}
		System.out.println("============================= memcoulist");
		System.out.println(memCouList);
		return memCouList;
	}

	@Override
	public int checkPrice(int memCouid, int price) {
		Coupon coupon = memCouRepo.findById(memCouid).get().getCoupon();

		int dis = coupon.getDiscount();
		int result = 0;
		int a = (price * dis) / 100;
		
		if(price>=coupon.getMinPrice()) {
			if(coupon.getDiscount()>=100) {
				result = dis;
			}else if(dis < 100) {
				result = a;
				System.out.println("할인적용금액 : " +a);
				if(a > coupon.getMaxValue()) {
					result = coupon.getMaxValue();
				}
			}
		}
		
		return result;
	}
	
}
