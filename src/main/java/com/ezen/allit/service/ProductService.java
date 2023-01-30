package com.ezen.allit.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import com.ezen.allit.domain.Product;

public interface ProductService {
	Page<Product> getProductList(Pageable pageable);
	
	Product getProduct(Product product);
	
	void saveProduct(Product product, MultipartFile file) throws Exception;
	
	void modifyProduct(int pno, Product product, MultipartFile imageFile) throws Exception;
	
	void deleteProduct(int pno);
	
	Page<Product> search(String searchKeyword, Pageable pageable);
}
