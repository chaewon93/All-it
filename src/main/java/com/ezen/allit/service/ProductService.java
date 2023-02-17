package com.ezen.allit.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ezen.allit.domain.Product;

public interface ProductService {	
	Page<Product> getProductList(Pageable pageable);
	
	Page<Product> getNewProductList(Pageable pageable);
	
	Page<Product> getMdpickProductList(Pageable pageable);
	
	List<Product> getMdpickProductMainPage();
	
	Product getProduct(int pno);
	
	void updateCount(int pno);
	
	Page<Product> search(String searchKeyword, Pageable pageable);
	
	Page<Product> search(int searchCondition, String searchKeyword, Pageable pageable);
	
	Page<Product> search(int searchCondition, Pageable pageable);
}
