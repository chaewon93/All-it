package com.ezen.allit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezen.allit.domain.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Integer> {

	Reply findReplyByRno(int rno);
}
