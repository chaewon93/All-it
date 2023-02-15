package com.ezen.allit.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ezen.allit.domain.Product;
import com.ezen.allit.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
	private final ProductRepository productRepo;
	
	/*
	 * 상품목록조회
	 */
	@Transactional
	public Page<Product> getProductList(Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 6;

		Page<Product> product = 
				productRepo.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "pno")));

        return product;
	}
	
	/*
	 * 신상품목록조회
	 */
	@Transactional
	public Page<Product> getNewProductList(Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 6;

		Page<Product> product = 
				productRepo.getNewProductList(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "pno")));

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
	 * 상품 조회수 증가
	 */
	@Transactional
	public void updateCount(int pno) {
		Product product = productRepo.findById(pno).get();
		product.setCount(product.getCount()+1);
	}
	
	/*
	 * 상품검색 (검색조건 x, 검색어 o)
	 */
	@Transactional
	public Page<Product> search(String searchKeyword, Pageable pageable) {
		int page = pageable.getPageNumber() - 1;

		int pageSize = 6;

		Page<Product> product = 
				productRepo.findAllByNameContaining(searchKeyword, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "pno")));
		
		return product;
	}
	
	/*
	 * 상품검색 (검색조건 o, 검색어 o)
	 */
	@Transactional
	public Page<Product> search(int searchCondition, String searchKeyword, Pageable pageable) {
		int page = pageable.getPageNumber() - 1;

		int pageSize = 6;

		Page<Product> product = 
				productRepo.findAllByCategoryAndNameContaining(searchCondition, searchKeyword, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "pno")));
		
		return product;
	}
	
	/*
	 * 상품검색 (검색조건 o, 검색어 x), 카테고리별 상품목록조회
	 */
	@Transactional
	public Page<Product> search(int searchCondition, Pageable pageable) {
		int page = pageable.getPageNumber() - 1;

		int pageSize = 6;

		Page<Product> product = 
				productRepo.findAllByCategory(searchCondition, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "pno")));
		
		return product;
	}
	
}














