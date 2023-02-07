package com.ezen.allit.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ezen.allit.domain.QnA;

public interface QnARepository extends JpaRepository<QnA, Integer> {

	Page<QnA> findNoQnAByStatus(int status, Pageable pageable);
	
	QnA findQnAByQno(int qno);
	
	Page<QnA> findCateQnAByCategoryContaining(String cate, Pageable pageable);
	
	// 상품문의 작성
	@Modifying
	@Query(value = "INSERT INTO qna(pno, sid, mid, category, content, status, rno, reg_date) VALUES(?1, ?2, ?3, ?4, ?5, 0, '', sysdate)", nativeQuery = true)
	int saveQnA(int pno, String sid, String mid, String category, String content);
}
