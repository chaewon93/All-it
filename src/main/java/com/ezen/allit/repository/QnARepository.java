package com.ezen.allit.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.QnA;

public interface QnARepository extends JpaRepository<QnA, Integer> {

	Page<QnA> findNoQnAByStatus(int status, Pageable pageable);
	
	QnA findQnAByQno(int qno);
	
	Page<QnA> findCateQnAByCategoryContaining(String cate, Pageable pageable);
	
	// 1:1 문의 내역 조회
	// SELECT * FROM qna WHERE mid = ? AND category not in ('상품문의')
	Page<QnA> findByMemberAndCategoryNotIn(Member member, List<String> cate, Pageable pageable);
	// @Query(value = "SELECT * FROM qna q WHERE q.mid = ?1 AND (q.pno is null OR q.title is not null)", nativeQuery = true)
	// Page<QnA> getQnaList(Member member, Pageable pageable);
}	
