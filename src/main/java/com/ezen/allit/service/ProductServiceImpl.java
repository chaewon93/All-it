package com.ezen.allit.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ezen.allit.domain.Hit;
import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.Product;
import com.ezen.allit.domain.Seller;
import com.ezen.allit.dto.HitSaveRequestDto;
import com.ezen.allit.repository.HitRepository;
import com.ezen.allit.repository.MemberRepository;
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














