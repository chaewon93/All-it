package com.ezen.allit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ezen.allit.domain.Product;
import com.ezen.allit.domain.Review;
import com.ezen.allit.service.ProductService;

@Controller
@RequestMapping("/product/")
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	/** 카테고리별 상품목록 조회 */
	@GetMapping("/category/{category}")
	public String getProductListByCategory(@PageableDefault(page = 1)Pageable pageable,
										Model model,
										@PathVariable int category) {
		Page<Product> productList = productService.getProductListByCategory(category, pageable);
		
		int naviSize = 10; // 페이지네이션 갯수
		int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / naviSize))) - 1) * naviSize + 1; // 1 11 21 31 ~~
		int endPage = ((startPage + naviSize - 1) < productList.getTotalPages()) ? startPage + naviSize - 1 : productList.getTotalPages();
		
		model.addAttribute("productList", productList);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		
		return "product/list";
	}

	/** 신상품 조회 */
	@GetMapping("/newItem")
	public String newItemList(Model model, @PageableDefault(page = 1) Pageable pageable,
							@RequestParam(value= "searchKeyword", defaultValue = "") String searchKeyword) {		
		
		Page<Product> productList = null;

		if(searchKeyword.equals("")) {
			System.out.println("검색 키워드 없음");
			productList = productService.getProductList(pageable);
		} else {
			System.out.printf("검색 키워드: [%s]\n", searchKeyword);
			productList = productService.search(searchKeyword, pageable);
		}

		int naviSize = 10; // 페이지네이션 갯수
		int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / naviSize))) - 1) * naviSize + 1; // 1 11 21 31 ~~
		int endPage = ((startPage + naviSize - 1) < productList.getTotalPages()) ? startPage + naviSize - 1 : productList.getTotalPages();
		
		model.addAttribute("productList", productList);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);

		return "product/list";
	}
	
	/** 베스트상품 조회 */
	
	/** 상품 상세조회 */
	@GetMapping("/{pno}")
	public String getProduct(@PathVariable int pno, Model model,
							@PageableDefault(page = 1) Pageable pageable) {
		Product theProduct = productService.getProduct(pno);
		
		/* 조회수 증가 */
		productService.updateCount(theProduct.getPno());
		
		/* 별점 평균 구하기 */
		List<Review> reviewList = theProduct.getReview();
		if(reviewList != null) {
			Review review = null;
			int totalRating = 0;
			for(int i=0; i<reviewList.size(); i++) {
				review = reviewList.get(i);
				totalRating += review.getRating();
			}
			float rating = (float)totalRating/reviewList.size();
			theProduct.setRating(getRating(rating, 1));
		} 
		if(reviewList.isEmpty()) {
			theProduct.setRating(0);
		}
		
		model.addAttribute("product", theProduct);
		model.addAttribute("page", pageable.getPageNumber());
		
		return "product/detail";
	}
	
	/** 별점 자릿수 계산 매서드 */
	public static float getRating(float rating, int position) {
		float num = (float) Math.pow(10.0, position);
		
		return (Math.round(rating * num) / num);
	}
	
}
