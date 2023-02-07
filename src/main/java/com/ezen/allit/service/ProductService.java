package com.ezen.allit.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ezen.allit.domain.Product;

public interface ProductService {
	Page<Product> getProductListByCategory(int category, Pageable pageable);
	
	Page<Product> getProductList(Pageable pageable);
	
	Product getProduct(int pno);
	
	void updateCount(int pno);
	
	Page<Product> search(String searchKeyword, Pageable pageable);
}
