package com.ezen.allit.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.allit.domain.QnA;

public interface QnARepository extends JpaRepository<QnA, Integer> {

	Page<QnA> findNoQnAByStatus(int status, Pageable pageable);
	
	QnA findQnAByQno(int qno);
	
	Page<QnA> findCateQnAByCategoryContaining(String cate, Pageable pageable);

}
