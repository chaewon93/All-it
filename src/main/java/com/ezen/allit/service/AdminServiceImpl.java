package com.ezen.allit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.QnA;
import com.ezen.allit.domain.Reply;
import com.ezen.allit.repository.MemberRepository;
import com.ezen.allit.repository.QnARepository;
import com.ezen.allit.repository.ReplyRepository;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private MemberRepository memberRepo;
	
	@Autowired
	private QnARepository qnaRepo;
	
	@Autowired
	private ReplyRepository repRepo;
	
	@Override
	public List<Member> getMemberList() {

		List<Member> getMemberList = memberRepo.findAll();
		if(!getMemberList.isEmpty())
			return getMemberList;
		else return null;
	}

	@Override
	public List<QnA> getQnAList() {

		List<QnA> getQnAList = qnaRepo.findAll();
		
		System.out.println("-------------------------------------");
		System.out.println(getQnAList);
		System.out.println("-------------------------------------");
		
		if(!getQnAList.isEmpty())
			return getQnAList;
		else return null;
	}

	@Override
	public void insertReply(QnA temp, Reply rep) {

		QnA qna = qnaRepo.findQnAByQno(temp.getQno());
		
		System.out.println("------------------------------------- QnA");
		System.out.println(qna);
		System.out.println("------------------------------------- QnA");		
		
		rep.setMember(qna.getMember());
		rep.setProduct(qna.getProduct());
		rep.setSeller(qna.getSeller());
		
		System.out.println("-------------------------------------");
		System.out.println(rep);
		System.out.println("-------------------------------------");		
		
		qna.setStatus("1");
		qna.setReply(rep);
					
		repRepo.save(rep);
		qnaRepo.save(qna);
		
		System.out.println("------------------------------------- finally QnA");
		System.out.println(qna);
		System.out.println("------------------------------------- finally QnA");			
		
		
	}

	@Override
	public void updateReply(Reply rep) {
		
		Reply repl = repRepo.findReplyByRno(rep.getRno());
		
		System.out.println("------------------------------------- repl");
		System.out.println(repl);
		System.out.println("------------------------------------- repl");		
		
		repl.setContent(rep.getContent());
		
		repRepo.save(repl);
	}

	@Override
	public void deleteReply(int qno) {

		QnA qna = qnaRepo.findQnAByQno(qno);
		
		int rno = qna.getReply().getRno();
		
		qna.setReply(null);
		qna.setStatus("0");
		
		repRepo.deleteById(rno);
	}
	
	

}
