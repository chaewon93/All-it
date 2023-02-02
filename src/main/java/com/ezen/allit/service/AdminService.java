package com.ezen.allit.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.Product;
import com.ezen.allit.domain.QnA;
import com.ezen.allit.domain.Reply;

public interface AdminService {
	
	List<Member> getMemberList();
	
	List<QnA> getQnAList();
	
	void insertReply(QnA temp, Reply rep);
	
	void updateReply(Reply rep);
	
	void deleteReply(int qno);
	
	Page<Product> findProductByStatus(int status, Pageable pageable);
	
}
