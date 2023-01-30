package com.ezen.allit.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.ezen.allit.domain.Product;
import com.ezen.allit.domain.Seller;

public interface SellerService {

	public Seller findByIdAndPwd(String id, String pwd);
	
	void saveSeller(Seller seller);
	
	Page<Product> getProductList(Pageable pageable);
	
	Product getProduct(int pno);
	
	Page<Product> search(String searchKeyword, Pageable pageable);
	
	Seller getSeller(Seller seller);

	void saveProduct(Product product, MultipartFile imageFile) throws Exception;
	
	void modifyProduct(int pno, Product product, MultipartFile imageFile) throws Exception;
	
	void deleteProduct(int pno);
}
