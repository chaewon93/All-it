package com.ezen.allit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.allit.domain.QnA;

public interface QnARepository extends JpaRepository<QnA, Integer> {

	List<QnA> findQnAByStatus(String status);
	
	QnA findQnAByQno(int qno);

}
