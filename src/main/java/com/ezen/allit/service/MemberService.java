package com.ezen.allit.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.OrdersDetail;
import com.ezen.allit.domain.QnA;
import com.ezen.allit.dto.HitSaveRequestDto;

public interface MemberService {
	
	/* <== 회원 정보 ==> */
	public Member getMember(Member member);
	
	public void saveMember(Member member);
	
	public int idCheck(String id);
	
	public Member findById(Member member); 
	
	public Member findByPw(Member member); 
	
	public void deleteMember(String id);
	
	/* <== 상품 주문 ==> */
	/** 상품 구매시 올잇머니 차감 */
	void minusMoney(String id, int amount);
	
	/** 상품 구매시 포인트 사용 */
	void minusPoint(String id, int amount);
	
	/** 구매확정 후 포인트 적립 */
	void addPoint(String id, int amount);

	/* <== 1:1문의(QnA) ==> */
	public void saveQna(QnA qna);
	
	public Page<QnA> getQnaList(Member member, Pageable pageable);
	
	public QnA getQnaDetail(int qno);
	
	void hitProduct(HitSaveRequestDto hitSaveRequestDto); // 상품 좋아요
}
