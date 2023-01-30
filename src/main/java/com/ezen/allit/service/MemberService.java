package com.ezen.allit.service;

import com.ezen.allit.domain.Member;

public interface MemberService {
	
	public Member getMember(Member member);
	
	public void saveMember(Member member);
	
	public int idCheck(String id);
	
	public String findById(String name, String email); 
	
	public String findByPw(String id, String name, String email); 
	
	public void deleteMember(String id);
}
