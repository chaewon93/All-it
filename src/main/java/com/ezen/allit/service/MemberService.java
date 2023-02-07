package com.ezen.allit.service;

import com.ezen.allit.domain.Member;
import com.ezen.allit.dto.HitSaveRequestDto;

public interface MemberService {
	
	public Member getMember(Member member);
	
	public void saveMember(Member member);
	
	public int idCheck(String id);
	
	public Member findById(Member member); 
	
	public Member findByPw(Member member); 
	
	public void deleteMember(String id);
	
	void hitProduct(HitSaveRequestDto hitSaveRequestDto); // 상품 좋아요
}
