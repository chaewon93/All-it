package com.ezen.allit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.allit.domain.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {

	// 답변 번호로 답변 찾기
	Reply findReplyByRno(int rno);
}
