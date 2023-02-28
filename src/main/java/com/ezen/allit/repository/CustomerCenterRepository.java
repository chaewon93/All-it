package com.ezen.allit.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.allit.domain.CustomerCenter;

public interface CustomerCenterRepository extends JpaRepository<CustomerCenter, Integer> {

//	List<CustomerCenter> findCustomerCenterByCategoryContaining(String cate);
	
	// 번호로 고객센터 글 찾기
	CustomerCenter findCustomerCenterByCno(int cno);
	
	// 카테고리별 고객센터 글 조회
	Page<CustomerCenter> findCustomerCenterByCategoryContaining(String cate, Pageable pageable);
	
	// 메인화면 노출 여부 구분 조회
	List<CustomerCenter> findCustomerCenterByPick(String pick);
	
}
