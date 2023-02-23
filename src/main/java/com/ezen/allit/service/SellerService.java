package com.ezen.allit.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.OrdersDetail;
import com.ezen.allit.domain.Product;
import com.ezen.allit.domain.QnA;
import com.ezen.allit.domain.Seller;
import com.ezen.allit.dto.MemberDto;

public interface SellerService {

	Seller findByIdAndPwd(String id, String pwd);
	
	void modify(Seller seller);
	
	void quit(Seller seller);
	
	void saveSeller(Seller seller);

	Page<Product> getProductList(Pageable pageable, Seller seller);
	
	Page<Product> search(Seller seller, String searchKeyword, Pageable pageable);

	Page<Product> search(Seller seller, int searchCondition, Pageable pageable);
	
	Page<Product> search(Seller seller, String searchKeyword, int searchCondition, Pageable pageable);
	
	Product getProduct(int pno);
	
	Page<Product> getUnregisteredProductList(Seller seller, Pageable pageable);
	
	Page<OrdersDetail> getOrderList(Seller seller, Pageable pageable);
	
	Page<OrdersDetail> getSearhcedOrderList(Seller seller, String searchKeyword, Pageable pageable);
	
	Page<QnA> getQnAList(Seller seller, Pageable pageable);
	
	Page<QnA> getSearchedQnAList(Seller seller, String searchKeyword, Pageable pageable);
	
	QnA getQnA(int qno);
	
	Seller getSeller(Seller seller);

	void saveProduct(Product product, MultipartFile imageFile) throws Exception;
	
	void modifyProduct(int pno, Product product, MultipartFile imageFile) throws Exception;
	
	void deleteProduct(int pno);
	
	public int idCheck(String id);
	
	public boolean pwdCheck(Seller seller);
	
	public Seller modifySellerPwd(Seller seller);
}
