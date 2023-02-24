package com.ezen.allit.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.ezen.allit.config.auth.PrincipalDetailMember;
import com.ezen.allit.config.auth.PrincipalDetailSeller;
import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.Product;
import com.ezen.allit.domain.Review;
import com.ezen.allit.dto.SearchDto;
import com.ezen.allit.service.ProductService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
@SessionAttributes("user")
public class ProductController {
	private final ProductService productService;
	
	@ModelAttribute("user")
	public Member setMember(@AuthenticationPrincipal PrincipalDetailMember principal) {
		if(principal != null) {
			Member member = principal.getMember();
			return member;
		} else {
			return null;
		}
	}

	/** 상품 검색 조회 */
	@GetMapping("/")
	public String getsearchList(Model model,
							@PageableDefault(page = 1) Pageable pageable,
							@RequestParam(value= "searchCondition", defaultValue = "0") int searchCondition,
							@RequestParam(value= "searchKeyword", defaultValue = "") String searchKeyword) {		
		
		/* productList null값으로 전역변수 선언 */
		Page<Product> productList = null;
		
		/* SearchDto라는 검색만을 위한 dto를 만들어 사용 */
		SearchDto search = new SearchDto();
		search.setSearchCondition(searchCondition);
		search.setSearchKeyword(searchKeyword);
	
		if(searchCondition == 0) { 		   // 검색 조건이 없을 경우
			if(searchKeyword.equals("")) { // 검색어가 없을 경우
				productList = productService.getProductList(pageable);
			} else { 					   // 검색어가 있을 경우
				productList = productService.search(searchKeyword, pageable);
			} 
		} else { 				   		   // 검색 조건이 있을 경우
			if(searchKeyword.equals("")) { // 검색어가 없을 경우
				productList = productService.search(searchCondition, pageable);
			} else { 					   // 검색어가 있을 경우
				productList = productService.search(searchCondition, searchKeyword, pageable);
			}			
		}

		int naviSize = 10; // 페이지네이션 갯수
		int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / naviSize))) - 1) * naviSize + 1; // 1 11 21 31 ~~
		int endPage = ((startPage + naviSize - 1) < productList.getTotalPages()) ? startPage + naviSize - 1 : productList.getTotalPages();
		
		model.addAttribute("list", productList);
		model.addAttribute("url", "/product/");
		model.addAttribute("productList", productList);
		model.addAttribute("search", search);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);

		return "product/list";
	}

	/** 신상품 조회 */
	@GetMapping("/new")
	public String getNewList(Model model,
							@PageableDefault(page = 1) Pageable pageable,
							@RequestParam(value= "searchCondition", defaultValue = "0") int searchCondition,
							@RequestParam(value= "searchKeyword", defaultValue = "") String searchKeyword) {		
		
		/* productList null값으로 전역변수 선언 */
		Page<Product> productList = null;
		
		/* SearchDto라는 검색만을 위한 dto를 만들어 사용 */
		SearchDto search = new SearchDto();
		search.setSearchCondition(searchCondition);
		search.setSearchKeyword(searchKeyword);

		if(searchKeyword.equals("")) { // 검색어가 없을 경우
			productList = productService.getNewProductList(pageable);
		} else {					   // 검색어가 있을 경우
			productList = productService.search(searchCondition, searchKeyword, pageable);
		}

		int naviSize = 10; // 페이지네이션 갯수
		int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / naviSize))) - 1) * naviSize + 1; // 1 11 21 31 ~~
		int endPage = ((startPage + naviSize - 1) < productList.getTotalPages()) ? startPage + naviSize - 1 : productList.getTotalPages();
		
		model.addAttribute("list", productList);
		model.addAttribute("url", "/product/");
		model.addAttribute("productList", productList);
		model.addAttribute("search", search);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);

		return "product/list";
	}
	
	/** 베스트상품 조회 */
	@GetMapping("/best")
	public String getBestList(Model model,
							@PageableDefault(page = 1) Pageable pageable,
							@RequestParam(value= "searchCondition", defaultValue = "0") int searchCondition,
							@RequestParam(value= "searchKeyword", defaultValue = "") String searchKeyword) {		
		
		/* productList null값으로 전역변수 선언 */
		Page<Product> productList = null;
		
		/* SearchDto라는 검색만을 위한 dto를 만들어 사용 */
		SearchDto search = new SearchDto();
		search.setSearchCondition(searchCondition);
		search.setSearchKeyword(searchKeyword);

		if(searchKeyword.equals("")) { // 검색어가 없을 경우
			productList = productService.getBestProductList(pageable);
		} else {					   // 검색어가 있을 경우
			productList = productService.search(searchCondition, searchKeyword, pageable);
		}

		int naviSize = 10; // 페이지네이션 갯수
		int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / naviSize))) - 1) * naviSize + 1; // 1 11 21 31 ~~
		int endPage = ((startPage + naviSize - 1) < productList.getTotalPages()) ? startPage + naviSize - 1 : productList.getTotalPages();
		
		model.addAttribute("list", productList);
		model.addAttribute("url", "/product/");
		model.addAttribute("productList", productList);
		model.addAttribute("search", search);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);

		return "product/list";
	}
	
	/** 특가세일상품 조회 */
	@GetMapping("/sale")
	public String getSaleList(Model model,
							@PageableDefault(page = 1) Pageable pageable,
							@RequestParam(value= "searchCondition", defaultValue = "0") int searchCondition,
							@RequestParam(value= "searchKeyword", defaultValue = "") String searchKeyword) {		
		
		/* productList null값으로 전역변수 선언 */
		Page<Product> productList = null;
		
		/* SearchDto라는 검색만을 위한 dto를 만들어 사용 */
		SearchDto search = new SearchDto();
		search.setSearchCondition(searchCondition);
		search.setSearchKeyword(searchKeyword);

		if(searchKeyword.equals("")) { // 검색어가 없을 경우
			productList = productService.getSaleProductList(pageable);
		} else {					   // 검색어가 있을 경우
			productList = productService.search(searchCondition, searchKeyword, pageable);
		}

		int naviSize = 10; // 페이지네이션 갯수
		int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / naviSize))) - 1) * naviSize + 1; // 1 11 21 31 ~~
		int endPage = ((startPage + naviSize - 1) < productList.getTotalPages()) ? startPage + naviSize - 1 : productList.getTotalPages();
		
		model.addAttribute("list", productList);
		model.addAttribute("url", "/product/sale");
		model.addAttribute("productList", productList);
		model.addAttribute("search", search);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);

		return "product/list";
	}
	
	/** MDPICK 상품 조회 */
	@GetMapping("/mdpickList")
	public String mdpcikList(Model model, @PageableDefault(page = 1) Pageable pageable,
							@RequestParam(value= "searchCondition", defaultValue = "0") int searchCondition,
							@RequestParam(value= "searchKeyword", defaultValue = "") String searchKeyword) {
		
		Page<Product> productList = null;

		if(searchKeyword.equals("")) {

			productList = productService.getMdpickProductList(pageable);
		} else {
			System.out.printf("검색 키워드: [%s]\n", searchKeyword);
//			productList = productService.search(searchCondition, searchKeyword, pageable);
		}

		int naviSize = 10; // 페이지네이션 갯수
		int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / naviSize))) - 1) * naviSize + 1; // 1 11 21 31 ~~
		int endPage = ((startPage + naviSize - 1) < productList.getTotalPages()) ? startPage + naviSize - 1 : productList.getTotalPages();

		model.addAttribute("list", productList);
		model.addAttribute("url", "/product/mdpickList");
		model.addAttribute("productList", productList);
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);
		
		return "product/mdpickList";
	}
	
	/** 상품 상세조회 */
	@GetMapping("/{pno}")
	public String getProduct(Model model,
							@PathVariable int pno,
							@AuthenticationPrincipal PrincipalDetailSeller principal,
							@PageableDefault(page = 1) Pageable pageable) {
		Product theProduct = productService.getProduct(pno);
		
		/* 별점 평균 구하기 */
		List<Review> reviewList = theProduct.getReview();
		if(reviewList != null) {
			Review review = null;
			int totalRating = 0;
			for(int i=0; i<reviewList.size(); i++) { // 리뷰갯수만큼 반복문을 돌려
				review = reviewList.get(i);
				totalRating += review.getRating(); // totalRating에 저장
			}
			float rating = (float)totalRating/reviewList.size(); // 리뷰갯수에서 totalRating을 나누어 최종적으로 값을 저장
			theProduct.setRating(getRating(rating, 1));
		} 
		if(reviewList.isEmpty()) {
			theProduct.setRating(0);
		}
		
		/* 사용자가 조회할 시에만 조회수 증가 (관리자가 조회할 시에는 카운트 x) */		
		if(principal == null) {
			productService.updateCount(theProduct.getPno());			
		}		

		Member member = (Member) model.getAttribute("user");
		
		model.addAttribute("user", member);
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
