package com.ezen.allit.service;

import org.springframework.web.multipart.MultipartFile;

import com.ezen.allit.domain.Product;
import com.ezen.allit.domain.Seller;

public interface SellerService {

	public Seller findByIdAndPwd(String id, String pwd);
	
	void saveSeller(Seller seller);
	
	Seller getSeller(Seller seller);

	void saveProduct(Product product, MultipartFile imageFile) throws Exception;
	
	void modifyProduct(int pno, Product product, MultipartFile imageFile) throws Exception;
	
	void deleteProduct(int pno);
}
