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

	/** 쿠폰 목록 조회 */
	@Override
	public Page<Coupon> findCouponList(Pageable pageable) {
		
		int page = pageable.getPageNumber() - 1;
		int pageSize = 10;
		
		Page<Coupon> couponList = 
				couponRepo.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "couId")));

        return couponList;	
	}

	/** 쿠폰 생성 */
	@Override
	public void createCoupon(Coupon coupon) {
		
        Random random = new Random();
        int length = 12;

        StringBuffer word = new StringBuffer();
        for (int i = 0; i < length; i++) {
        	// 0~3 미만 정수형 난수 반환
            int choice = random.nextInt(3);
            switch(choice) {
            	// 난수에 따라 임의의 대문자, 소문자, 숫자 생성.. + 는 아스키 코드 사용
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
        coupon.setCouName(word.toString());

        couponRepo.save(coupon);
	}

	/** 쿠폰 수정 */
	@Override
	public void updateCoupon(Coupon coupon) {
		Coupon cou = couponRepo.findById(coupon.getCouId()).get();
		
		cou.setCouContent(coupon.getCouContent());
		cou.setEndDate(coupon.getEndDate());
		cou.setPeriod(coupon.getPeriod());
		cou.setDiscount(coupon.getDiscount());
		cou.setMinPrice(coupon.getMinPrice());
		cou.setMaxValue(coupon.getMaxValue());
		cou.setCondition(coupon.getCondition());
		
		couponRepo.save(cou);
	}	
  
	// 멤버가 쿠폰 다운로드 했을 때 memCou에 쿠폰 등록
	/** 쿠폰 다운로드(사용자) */
	@Override
	public void insertMemCoupon(Member member, int couid) {
		
		System.out.println("============================ MDPICK 쿠폰 테스트");
		Coupon coupon = couponRepo.findById(couid).get();
		System.out.println(coupon);
		
		MemCoupon memCoupon = new MemCoupon();
		memCoupon.setMember(member);
		memCoupon.setCoupon(coupon);
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(memCoupon.getCreateMemCouDate());
		// 쿠폰 만료일 계산
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

	// 회원이 받을 수 있는 쿠폰 조회 - 상품이 없을 때, 있을 때
	@Override
	public List<Coupon> forMemberCouponList(Member member, int pno) {

		System.out.println("=============================================== 상품이 있는지 없는지");
		System.out.println(pno);
		if(pno == 0) {
			// 상품이 없을 때 - 마이페이지에서 쿠폰 조회 시
			// 등급과 성별 조건 만족하는 쿠폰 조회
			// 검색가능 쿠폰 조회
			List<Coupon> allCouList = couponRepo.findCouponByConditionContaining("show");
			
			List<Coupon> allCouList1 = couponRepo.findCouponByConditionContainingOrConditionContaining(member.getGrade().toString(), "ALL");
			
			allCouList.retainAll(allCouList1);
			
			List<Coupon> allCouList2 = couponRepo.findCouponByConditionContainingOrConditionContaining(member.getGender(), "남녀");
			
			allCouList.retainAll(allCouList2);
			return allCouList;
		}else {
			// 상품이 있을 때 - 상품페이지 or 주문페이지
			// 등급과 성별로 조회한 쿠폰 리스트 중 중복 제거
			List<Coupon> allCouList = couponRepo.findCouponByConditionContaining("show");
			
			List<Coupon> allCouList1 = couponRepo.findCouponByConditionContainingOrConditionContaining(member.getGrade().toString(), "ALL");
			
			allCouList.retainAll(allCouList1);
			
			List<Coupon> allCouList2 = couponRepo.findCouponByConditionContainingOrConditionContaining(member.getGender(), "남녀");
			
			allCouList.retainAll(allCouList2);

			// 카테고리로 조회한 쿠폰 리스트
			Product pro = proService.getProduct(pno);			
			List<Coupon> allCouList3 = couponRepo.findCouponByConditionContainingOrConditionContaining(Integer.toString(pro.getCategory()), "0");
			
			// 등급 성별로 걸러진 쿠폰리스트와 카테고리 쿠폰 리스트 중복 제거
			allCouList.retainAll(allCouList3);
			
			// MDPICK 조건 쿠폰 조회
			if(pro.getMdPickyn() == 0) {
				// 상품이 MDPICK 아니면 NO 인 쿠폰만 조회해서 중복 제거
				List<Coupon> allCouList4 = couponRepo.findCouponByConditionContaining("NO");
				allCouList.retainAll(allCouList4);
			}else if(pro.getMdPickyn() == 1) {
				// 상품이 MDPICK 이면 YES와 NO 모두 조건에 맞으므로 따로 과정 없음

			}
			
			// 판매자 조건 쿠폰 조회 후 중복 제거
			List<Coupon> allCouList5 = couponRepo.findCouponByConditionContainingOrConditionContaining(pro.getSeller().getId(), "SELLERS");
			allCouList.retainAll(allCouList5);
			
			return allCouList;
		}

	}

	// 가지고 있는 쿠폰 중 상품에 사용 가능한 쿠폰 조회
	@Override
	public List<MemCoupon> MemProCouponList(Member member, int pno) {
		List<MemCoupon> list = member.getMemCoupon();
		Product pro = proService.getProduct(pno);
		List<MemCoupon> memCouList = new ArrayList<>();
		System.out.println("============================ Integer.toString(pro.getMdPickyn())"+Integer.toString(pro.getMdPickyn()));
		for(MemCoupon memCoupon : list) {
			String MdPick = "";
			if((pro.getMdPickyn()) == 1) {
				MdPick = "YES";
			}else if((pro.getMdPickyn()) == 0) {
				MdPick = "NO";
			}			 
			// 내가 가진 쿠폰 리스트에서 카테고리, MDPICK, 판매자 조건으로 사용 가능 조건 쿠폰 조회
			if(memCoupon.getCoupon().getCondition().contains(Integer.toString(pro.getCategory())) || memCoupon.getCoupon().getCondition().contains("0")){
				if(memCoupon.getCoupon().getCondition().contains(MdPick) || memCoupon.getCoupon().getCondition().contains("NO")) {
					if(memCoupon.getCoupon().getCondition().contains(pro.getSeller().getId()) || memCoupon.getCoupon().getCondition().contains("SELLERS")) {
						memCouList.add(memCoupon);
					}
				}
			}
		}
		return memCouList;
	}
	
	@Override
	public Coupon findRegCoupon(String couponName) {
		
		Coupon a = couponRepo.findCouponBycouName(couponName);
		
		if(a != null) {
			return a;
		}else {
			return null;
		}

	}

	// 상품 가격과 쿠폰 조건(최소사용금액/최대할인금액) 비교해서 할인 가격 선정
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

	/** 취소/반품시 쿠폰 복원 */
	@Transactional
	@Override
	public void updateStatus(int memCouid, int status) {
		MemCoupon memCoupon = memCouRepo.findById(memCouid).get();
		memCoupon.setStatus(status);
	}

}
