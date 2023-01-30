package com.ezen.allit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ezen.allit.domain.Product;
import com.ezen.allit.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductRepository productRepo;

	/*
	 * 상품목록조회
	 */
	@Transactional
	public Page<Product> getProductList(Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 3;
		
		Page<Product> product = 
				productRepo.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "pno")));
		
        System.out.println("product.getContent() = " + product.getContent()); 			  // 요청 페이지에 해당하는 글
        System.out.println("product.getTotalElements() = " + product.getTotalElements()); // 전체 글갯수
        System.out.println("product.getNumber() = " + product.getNumber()); 			  // DB로 요청한 페이지 번호
        System.out.println("product.getTotalPages() = " + product.getTotalPages()); 	  // 전체 페이지 갯수
        System.out.println("product.getSize() = " + product.getSize()); 				  // 한 페이지에 보여지는 글 갯수
        System.out.println("product.hasPrevious() = " + product.hasPrevious()); 		  // 이전 페이지 존재 여부
        System.out.println("product.isFirst() = " + product.isFirst()); 		  		  // 첫 페이지 여부
        System.out.println("product.isLast() = " + product.isLast()); 					  // 마지막 페이지 여부
		
        return product;
	}
	
	/*
	 * 상품조회
	 */
	@Transactional
	public Product getProduct(int pno) {
		return productRepo.findById(pno).get();
	}
	
	/*
	 * 상품검색
	 */
	@Transactional
	public Page<Product> search(String searchKeyword, Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 3;
		
		Page<Product> product = 
				productRepo.findByNameContaining(searchKeyword, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "pno")));
		
		return product;
	}
	
}














