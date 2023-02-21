package com.ezen.allit.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ezen.allit.domain.Member;
import com.ezen.allit.dto.AddressCount;
import com.ezen.allit.dto.Chart;
import com.ezen.allit.dto.GenderCount;

public interface MemberRepository extends JpaRepository<Member, String> {
	
	// 아이디 찾기
//	@Query(value = "SELECT id FROM member m WHERE m.name = ?1 AND m.email = ?2", nativeQuery = true)
//	String findById(String name, String email);
	Member findByNameAndEmail(String name, String email);
	
	// 비밀번호 찾기
//	@Query(value = "SELECT pwd FROM member m WHERE m.id = ?1 AND m.name = ?2 AND m.email = ?3", nativeQuery = true)
//	String findPw(String id, String name, String email);
	Member findByIdAndNameAndEmail(String id, String name, String email);

	Page<Member> findMemberByIdContaining(String searchKeyword, PageRequest pageRequest);
	
	int countMemberByGender(String gender);
	
//	@Query(value="select substr(address, 1, 2), COUNT(*) from member group by substr(address, 1, 2)", nativeQuery = true)
//	Map<String, Integer> chartAddressGroup();
	
//	@Query(value="select substr(address, 1, 2) from member group by substr(address, 1, 2)", nativeQuery = true)
//	List<Map<String, Object>> chartAddressGroup();
	
	// 차트에 필요한 쿼리
	@Query(value="select gender, COUNT(*) as gendercount from member group by gender", nativeQuery = true)
	List<GenderCount> chartGenderGroup();
	
	@Query(value="select substr(address, 1, 2) as address, COUNT(*) as addressCount from member group by substr(address, 1, 2)", nativeQuery = true)
	List<AddressCount> chartAddressGroup();
	
	@Query(value="select grade, COUNT(*) as count from member group by grade", nativeQuery = true)
	List<Chart> chartGradeGroup();
}
