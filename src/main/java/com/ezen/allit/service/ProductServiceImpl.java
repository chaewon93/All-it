package com.ezen.allit.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ezen.allit.domain.Product;
import com.ezen.allit.domain.Role;
import com.ezen.allit.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
	private final ProductRepository productRepo;
	
	/*
	 * 상품목록조회 (검색조건 x, 검색어 o)
	 */
	@Transactional
	public Page<Product> getProductList(Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 6;

		Page<Product> productList = 
				productRepo.findAllByStatusAndSellerRole(1, Role.SELLER, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "pno")));

        return productList;
	}
	
	/*
	 * 신상품목록조회
	 */
	@Transactional
	public Page<Product> getNewProductList(Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 6;

		Page<Product> productList = 
				productRepo.getNewProductList(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "pno")));

        return productList;
	}
	
	/*
	 * 베스트상품목록조회
	 */
	@Transactional
	public Page<Product> getBestProductList(Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 6;

		Page<Product> productList = 
				productRepo.getBestProductList(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "hit")));

        return productList;
	}
	
	/*
	 * 특가세일상품목록조회
	 */
	@Transactional
	public Page<Product> getSaleProductList(Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 6;

		Page<Product> productList = 
				productRepo.findAllByDiscountNotAndStatusAndSellerRole(0, 1, Role.SELLER, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "discount")));

        return productList;
	}
	
	/*
	 * MDPICK 상품 조회
	 */
	@Transactional
	public Page<Product> getMdpickProductList(Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 6;

		Page<Product> productList = 
				productRepo.findProductBymdPickyn(1, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "pno")));

        return productList;
	}
	
	@Transactional
	public List<Product> getMdpickProductMainPage() {

		List<Product> productList = 
				productRepo.findFirst3BymdPickynOrderByRegDateDesc(1);

        return productList;
	}
	
	/*
	 * 상품검색 (검색조건 x, 검색어 o)
	 */
	@Transactional
	public Page<Product> search(String searchKeyword, Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 6;

		Page<Product> productList = 
				productRepo.findAllByNameContainingAndStatusAndSellerRole(searchKeyword, 1, Role.SELLER, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "pno")));
		
		return productList;
	}
	
	/*
	 * 상품검색 (검색조건 o, 검색어 x), 카테고리별 상품목록조회
	 */
	@Transactional
	public Page<Product> search(int searchCondition, Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 6;

		Page<Product> productList = 
				productRepo.findAllByCategoryAndStatusAndSellerRole(searchCondition, 1, Role.SELLER, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "pno")));
		
		return productList;
	}
	
	/*
	 * 상품검색 (검색조건 o, 검색어 o)
	 */
	@Transactional
	public Page<Product> search(int searchCondition, String searchKeyword, Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 6;

		Page<Product> productList = 
				productRepo.findAllByCategoryAndNameContainingAndStatusAndSellerRole(searchCondition, searchKeyword, 1, Role.SELLER, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "pno")));
		
		return productList;
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
	
}














