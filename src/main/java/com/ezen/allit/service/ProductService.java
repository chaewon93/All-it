package com.ezen.allit.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ezen.allit.domain.Product;

public interface ProductService {
	Page<Product> getProductList(Pageable pageable);
	
	Product getProduct(int pno);
	
	Page<Product> search(String searchKeyword, Pageable pageable);
}
