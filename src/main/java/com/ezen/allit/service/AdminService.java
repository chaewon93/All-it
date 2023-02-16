package com.ezen.allit.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.Product;
import com.ezen.allit.domain.QnA;
import com.ezen.allit.domain.Reply;
import com.ezen.allit.domain.Role;
import com.ezen.allit.domain.Seller;

public interface AdminService {
	
	Page<Member> searchByAdminMem(String searchKeyword, Pageable pageable);
	
	Page<Member> getMemberList(Pageable pageable);
	
	Page<QnA> getQnAList(Pageable pageable);
	
//	Page<QnA> searchByAdminQna(String searchKeyword, Pageable pageable);
	
	Page<QnA> findQnAByStatus(int status, Pageable pageable);
	
//	Page<QnA> searchByAdminQnaStatus(int status, String searchKeyword, Pageable pageable);
	
	Page<QnA> findQnAByCategoryContaining(String cate, Pageable pageable);

	void insertReply(QnA temp, Reply rep);
	
	void updateReply(Reply rep);
	
	void deleteReply(int qno);
	
	Page<Product> findProductByStatus(int status, Pageable pageable);
	
//	Page<Product> searchByAdminPro(int status ,String searchKeyword, Pageable pageable);
	
	Page<Seller> findAllSeller(Pageable pageable);
	
	Page<Seller> findSellerByRole(Role role, Pageable pageable);
	
	Page<Seller> findSellerByRoleNot(Role role, Pageable pageable);
	
}
