package com.ezen.allit.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.Product;

public interface MemberRepository extends JpaRepository<Member, String> {
	
	// 아이디 찾기
	@Query(value = "SELECT id FROM member m WHERE m.name = ?1 AND m.email = ?2", nativeQuery = true)
	String findId(String name, String email);
	
	// 비밀번호 찾기
	@Query(value = "SELECT pwd FROM member m WHERE m.id = ?1 AND m.name = ?2 AND m.email = ?3", nativeQuery = true)
	String findPw(String id, String name, String email);

	Page<Member> findMemberByIdContaining(String searchKeyword, PageRequest pageRequest);
}
