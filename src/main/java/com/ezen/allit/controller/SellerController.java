package com.ezen.allit.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.ezen.allit.config.auth.PrincipalDetailSeller;
import com.ezen.allit.domain.OrdersDetail;
import com.ezen.allit.domain.Product;
import com.ezen.allit.domain.QnA;
import com.ezen.allit.domain.Review;
import com.ezen.allit.domain.Role;
import com.ezen.allit.domain.Seller;
import com.ezen.allit.dto.SearchDto;
import com.ezen.allit.service.SellerService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {
	private final SellerService sellerService;
	private final AuthenticationManager authenticationManager;
		
	// 판매자 상품등록 화면 이동
	@GetMapping("/insert")
	public String insertView() {
		
		return "seller/insert";
	}
	
	// 로그아웃
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		
		return "redirect:/";
	}
	
	/*
	 * 판매자 로그인
	 */
	@PostMapping("/login")
	public String login(Seller seller, HttpSession session) {
		Seller theSeller = sellerService.findByIdAndPwd(seller.getId(), seller.getPwd());
		if(theSeller != null) {
			if(theSeller.getRole().equals(Role.ADMIN)) {
				session.setAttribute("admin", theSeller);
				
				return "admin/adminMain";
			}
			if(theSeller.getRole().equals(Role.SELLER)) {
				session.setAttribute("seller", theSeller);
				return "redirect:/seller/";
			} else {
				
				return "seller/loginError";
			}
		} else {
			
			return "seller/login";
		}
	}
	
	// 판매자 마이페이지 이동
	@GetMapping("/mypage")
	public String mypageView(Model model,
						@AuthenticationPrincipal PrincipalDetailSeller principal) {
		
		Seller seller = principal.getSeller();
		System.out.println("seller = " + seller);

		String fullAddr = seller.getAddress();
		
		if(fullAddr != null) {
			String[] addr = fullAddr.split(",");
			model.addAttribute("addr", addr);
		}

		return "seller/mypage";

	}
	
	/*
	 * 판매자 정보 수정
	 */
	@PostMapping("/modify")
	public String modify(Seller seller) {
		sellerService.modify(seller);
		
		/* 수정한 정보로 세션 재주입 */
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(seller.getId(), seller.getPwd()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return "redirect:/seller/";
	}
	
	/*
	 * 판매자 탈퇴
	 */
	@PostMapping("/quit")
	public String quit(Seller seller) {
		sellerService.quit(seller);
		
		/* 세션 제거 */
		SecurityContextHolder.clearContext();
		
		return "redirect:/";
	}
	
	/*
	 * 판매자 메인화면 이동
	 */
	@RequestMapping("/")
	public String mainView(Model model,
						@RequestParam(value= "searchCondition", defaultValue = "0") int searchCondition,
						@RequestParam(value= "searchKeyword", defaultValue = "") String searchKeyword,
						@AuthenticationPrincipal PrincipalDetailSeller principal,
						@PageableDefault(page = 1) Pageable pageable) {
		
		/* productList null값으로 전역변수 선언 */
		Page<Product> productList = null; 
		
		/* SearchDto라는 검색만을 위한 dto를 만들어 사용 */
		SearchDto search = new SearchDto();
		search.setSearchCondition(searchCondition);
		search.setSearchKeyword(searchKeyword);
		
		if(searchCondition == 0) { 		   							// 검색 조건이 없을 경우
			if(searchKeyword == null || searchKeyword.equals("")) { // 검색어가 없을 경우
				productList = sellerService.getProductList(pageable, principal.getSeller());
			} else {				 								// 검색어가 있을 경우
				productList = sellerService.search(principal.getSeller(), searchKeyword, pageable);
			}
		} else { 	 				   		   						// 검색 조건이 있을 경우
			if(searchKeyword == null || searchKeyword.equals("")) { // 검색어가 없을 경우
				productList = sellerService.search(principal.getSeller(), searchCondition, pageable);
			} else {				 								// 검색어가 있을 경우
				productList = sellerService.search(principal.getSeller(), searchKeyword, searchCondition, pageable);
			}
		}
		
		int naviSize = 10; // 페이지네이션 갯수
		int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / naviSize))) - 1) * naviSize + 1; // 1 11 21 31 ~~
	    int endPage = ((startPage + naviSize - 1) < productList.getTotalPages()) ? startPage + naviSize - 1 : productList.getTotalPages();
		
	    model.addAttribute("list", productList);
	    model.addAttribute("url", "/seller/");
	    model.addAttribute("productList", productList);
	    model.addAttribute("search", search);
	    model.addAttribute("startPage", startPage);
	    model.addAttribute("endPage", endPage);
		
		return "seller/productList";
	}
	
	/*
	 * 판매자 미등록상품목록 조회
	 */
	@RequestMapping("/unregistered")
	public String getUnregisteredProductList(Model model,
						@AuthenticationPrincipal PrincipalDetailSeller principal,
						@PageableDefault(page = 1) Pageable pageable) {

		Page<Product> productList = sellerService.getUnregisteredProductList(principal.getSeller(), pageable);
	
		int naviSize = 10; // 페이지네이션 갯수
		int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / naviSize))) - 1) * naviSize + 1; // 1 11 21 31 ~~
	    int endPage = ((startPage + naviSize - 1) < productList.getTotalPages()) ? startPage + naviSize - 1 : productList.getTotalPages();
		
	    model.addAttribute("list", productList);
	    model.addAttribute("url", "/seller/unregistered");
	    model.addAttribute("productList", productList);
	    model.addAttribute("startPage", startPage);
	    model.addAttribute("endPage", endPage);
		
		return "seller/unRegisteredProductList";
	}
	
	/*
	 * 판매자 상품조회
	 */
	@GetMapping("/product/{pno}")
	public String getProduct(Model model,
							@PathVariable int pno,
							@PageableDefault(page = 1) Pageable pageable) {
		
		Product theProduct = sellerService.getProduct(pno);

		/* 별점 평균 구하기 */
		List<Review> reviewList = theProduct.getReview();
		
		if(reviewList != null) {
			Review review = null;
			int totalRating = 0;
			for(int i=0; i<reviewList.size(); i++) { // 리뷰갯수만큼 반복문을 돌려
				review = reviewList.get(i);
				totalRating += review.getRating();	 // totalRating에 저장
			}
			float rating = (float)totalRating/reviewList.size(); // 리뷰갯수에서 totalRating을 나누어 최종적으로 값을 저장
			theProduct.setRating(getRating(rating, 1));
		} 
		if(reviewList.isEmpty()) {
			theProduct.setRating(0);
		}
		
		model.addAttribute("product", theProduct);
		model.addAttribute("page", pageable.getPageNumber());
		
		return "seller/product";
	}
	
	/*
	 * 판매자 주문목록조회
	 */
	@GetMapping("/order")
	public String getOrderList(Model model,
							String searchKeyword,
							@AuthenticationPrincipal PrincipalDetailSeller principal,
							@PageableDefault(page = 1) Pageable pageable) {
		
		/* orderList null값으로 전역변수 선언 */
		Page<OrdersDetail> orderList = null;
		
		if(searchKeyword == null || searchKeyword.equals("")) { // 검색어가 없을 경우
			orderList = sellerService.getOrderList(principal.getSeller(), pageable);
		} else {				 								// 검색어가 있을 경우
			orderList = sellerService.getSearhcedOrderList(principal.getSeller(), searchKeyword, pageable);
		}

		int naviSize = 10; // 페이지네이션 갯수
		int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / naviSize))) - 1) * naviSize + 1; // 1 11 21 31 ~~
	    int endPage = ((startPage + naviSize - 1) < orderList.getTotalPages()) ? startPage + naviSize - 1 : orderList.getTotalPages();
		
	    model.addAttribute("list", orderList);
	    model.addAttribute("url", "/seller/order");
	    model.addAttribute("orderList", orderList);
	    model.addAttribute("startPage", startPage);
	    model.addAttribute("endPage", endPage);		
	    if(orderList.getTotalElements() == 0) model.addAttribute("size", 0);
	    System.out.println("orderList.size() = " + orderList.getSize());
		
		return "seller/orderList";
	}
	
	/*
	 * 판매자 qna목록조회
	 */
	@GetMapping("/qna")
	public String getQnAList(Model model,
							String searchKeyword,
							@AuthenticationPrincipal PrincipalDetailSeller principal,
							@PageableDefault(page = 1) Pageable pageable) {
		
		/* qnaList null값으로 전역변수 선언 */
		Page<QnA> qnaList = null;
		
		if(searchKeyword == null || searchKeyword.equals("")) { // 검색어가 있을 경우
			qnaList = sellerService.getQnAList(principal.getSeller(), pageable);
		} else {				 								// 검색어가 있을 경우
			qnaList = sellerService.getSearchedQnAList(principal.getSeller(), searchKeyword, pageable);
		}
		
		int naviSize = 10; // 페이지네이션 갯수
		int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / naviSize))) - 1) * naviSize + 1; // 1 11 21 31 ~~
	    int endPage = ((startPage + naviSize - 1) < qnaList.getTotalPages()) ? startPage + naviSize - 1 : qnaList.getTotalPages();
		
	    model.addAttribute("list", qnaList);
	    model.addAttribute("url", "/seller/qna");
	    model.addAttribute("qnaList", qnaList);
	    model.addAttribute("startPage", startPage);
	    model.addAttribute("endPage", endPage);		
	    if(qnaList.getTotalElements() == 0) model.addAttribute("size", 0);
	    
	    System.out.println("========= qnaList = " + qnaList);
		
		return "seller/qnaList";
	}
	
	/*
	 * 판매자 qna조회
	 */
	@GetMapping("/qna/{qno}")
	public String getQnA(@PathVariable int qno, Model model) {
		QnA qna = sellerService.getQnA(qno);
		model.addAttribute("qna", qna);
		
		return "seller/qna";
	}
	
	/*
	 * 판매자 상품등록
	 */
	@PostMapping("/product/insert")
	public String insertProduct(Product product, MultipartFile imageFile) throws Exception {
		sellerService.saveProduct(product, imageFile);
		
		return "redirect:/seller/";
	}
	
	/*
	 * 판매자 상품수정
	 */
	@PostMapping("/product/modify")
	public String modifyProduct(int pno, Product product, MultipartFile imageFile) throws Exception {
		sellerService.modifyProduct(pno, product, imageFile);
		
		return "redirect:/seller/";
	}
	
	/*
	 * 별점 자릿수 계산 매서드
	 */
	public static float getRating(float rating, int position) {
		float num = (float) Math.pow(10.0, position);
		
		return (Math.round(rating * num) / num);
	}

}












