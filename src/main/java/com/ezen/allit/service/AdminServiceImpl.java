package com.ezen.allit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.Product;
import com.ezen.allit.domain.QnA;
import com.ezen.allit.domain.Reply;
import com.ezen.allit.domain.Role;
import com.ezen.allit.domain.Seller;
import com.ezen.allit.repository.MemberRepository;
import com.ezen.allit.repository.ProductRepository;
import com.ezen.allit.repository.QnARepository;
import com.ezen.allit.repository.ReplyRepository;
import com.ezen.allit.repository.SellerRepository;

@Service
public class AdminServiceImpl implements AdminService {

	@Autowired
	private MemberRepository memberRepo;
	
	@Autowired
	private QnARepository qnaRepo;
	
	@Autowired
	private ReplyRepository repRepo;
	
	@Autowired
	private ProductRepository proRepo;
	
	@Autowired
	private SellerRepository sellerRepo;

	// 관리자 QnA 전체 조회
	@Override
	public Page<QnA> getQnAList(Pageable pageable) {

		int page = pageable.getPageNumber() - 1;
		int pageSize = 10;
		
		Page<QnA> qnaList = 
				qnaRepo.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "qno")));

		return qnaList;
	}

	// 관리자 QnA 답변(Reply) 작성
	@Override
	public void insertReply(QnA temp, Reply rep) {

		QnA qna = qnaRepo.findQnAByQno(temp.getQno());

		rep.setMember(qna.getMember());
		rep.setProduct(qna.getProduct());
		rep.setSeller(qna.getSeller());

		qna.setStatus(1);
		qna.setReply(rep);
					
		repRepo.save(rep);
		qnaRepo.save(qna);
		
	}
	
	// 관리자 QnA 답변(Reply) 수정
	@Override
	public void updateReply(Reply rep) {
		
		Reply repl = repRepo.findReplyByRno(rep.getRno());
	
		repl.setContent(rep.getContent());
		
		repRepo.save(repl);
	}

	// 관리자 QnA 답변(Reply) 삭제
	@Override
	public void deleteReply(int qno) {

		QnA qna = qnaRepo.findQnAByQno(qno);
		
		int rno = qna.getReply().getRno();
		
		qna.setReply(null);
		qna.setStatus(0);
		
		repRepo.deleteById(rno);
	}

	// 관리자 등록 상태에 따른 상품 조회(대기 or 등록)
	@Override
	public Page<Product> findProductByStatus(int status, Pageable pageable) {
		
		int page = pageable.getPageNumber() - 1;
		int pageSize = 10;
		
		Page<Product> product = 
				proRepo.findProductByStatus(status, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "pno")));
//				proRepo.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "pno")));
		
		return product;
	}
	
	// 관리자 등록 상태에 따른 상품 조회(대기 or 등록)와 키워드(name) 검색
//	@Override
//	public Page<Product> searchByAdminPro(int status, String searchKeyword, Pageable pageable) {
//		int page = pageable.getPageNumber() - 1;
//
//		int pageSize = 7;
//
//		Page<Product> product = 
//				proRepo.findByStatusAndNameContaining(status, searchKeyword, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "pno")));
//		
//		return product;
//	}

	// 관리자 회원 조회와 키워드(id) 검색
	@Override
	public Page<Member> searchByAdminMem(String searchKeyword, Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 10;

		Page<Member> member = 
				memberRepo.findMemberByIdContaining(searchKeyword, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id")));
		
		return member;
	}

	// 관리자 회원 조회
	@Override
	public Page<Member> getMemberList(Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 10;

		Page<Member> member = 
				memberRepo.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id")));

        return member;
	}

//	@Override
//	public Page<QnA> searchByAdminQna(String searchKeyword, Pageable pageable) {
//		int page = pageable.getPageNumber() - 1;
//		int pageSize = 6;
//
//		Page<QnA> qna = 
//				qnaRepo.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "qno")));
//
//        return qna;
//	}

	// 미답변, 답변 QnA 조회
	@Override
	public Page<QnA> findQnAByStatus(int status, Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 10;

		Page<QnA> qna = 
				qnaRepo.findNoQnAByStatus(status ,PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "qno")));

        return qna;
	}

//	@Override
//	public Page<QnA> searchByAdminQnaStatus(int status, String searchKeyword, Pageable pageable) {
//		int page = pageable.getPageNumber() - 1;
//		int pageSize = 6;
//
//		Page<QnA> qna = 
//				qnaRepo.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "qno")));
//
//        return qna;
//	}

	// 카테고리 별 QnA 조회
	@Override
	public Page<QnA> findQnAByCategoryContaining(String cate, Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 10;
		
		Page<QnA> qna = 
				qnaRepo.findCateQnAByCategoryContaining(cate, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "qno")));

        return qna;
	}

	// Seller Role 별 조회
	@Override
	public Page<Seller> findSellerByRole(Role role, Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 10;
		
		Page<Seller> seller = 
				sellerRepo.findSellerByRole(role, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id")));
		return seller;
	}

	// 관리자 빼고 Seller 조회
	@Override
	public Page<Seller> findSellerByRoleNot(Role role, Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 10;
		
		Page<Seller> seller = 
				sellerRepo.findSellerByRoleNot(role, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id")));
		return seller;
	}

	// Seller 전체 조회
	@Override
	public Page<Seller> findAllSeller(Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 10;
		
		Page<Seller> seller = 
				sellerRepo.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "id")));
		return seller;
	}

}
