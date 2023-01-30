package com.ezen.allit.service;

import java.util.List;

import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.QnA;
import com.ezen.allit.domain.Reply;

public interface AdminService {
	
	List<Member> getMemberList();
	
	List<QnA> getQnAList();
	
	void insertReply(QnA temp, Reply rep);
	
	void updateReply(Reply rep);
	
	void deleteReply(int qno);
	
}
