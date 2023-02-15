package com.ezen.allit.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

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

	// 상품문의 작성
	@Modifying
	@Query(value = "INSERT INTO qna(pno, sid, mid, category, title, content, status, rno, reg_date) VALUES(?1, ?2, ?3, ?4, ?5, ?6, 0, '', sysdate)", nativeQuery = true)
	int saveQnA(int pno, String sid, String mid, String category, String title, String content);
	
	// 상품문의 답변
	@Modifying
	@Query(value = "UPDATE qna SET response = ?1, res_date = sysdate WHERE qno=?2", nativeQuery = true)
	int responseQnA(String response, int qno);
	
	// 상품문의 답변삭제
	@Modifying
	@Query(value = "UPDATE qna SET response = '', res_date = null WHERE qno=?1", nativeQuery = true)
	int deleteResponseQnA(int qno);
	
	// 판매자 문의목록조회 (검색 x)
	Page<QnA> findAllBySellerId(String id, Pageable pageable);
	
	// 판매자 문의목록조회 (검색 o)
	Page<QnA> findAllBySellerIdAndTitleContaining(String id, String searchKeyword, Pageable pageable);	
}

