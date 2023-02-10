package com.ezen.allit.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ezen.allit.domain.OrdersDetail;
import com.ezen.allit.domain.Product;
import com.ezen.allit.domain.QnA;
import com.ezen.allit.domain.Review;
import com.ezen.allit.domain.Role;
import com.ezen.allit.domain.Seller;
import com.ezen.allit.service.SellerService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {
	private final SellerService sellerService;
		
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
	
	/** 판매자 아이디 중복확인 */
	@ResponseBody
	@PostMapping("/idCheck")
	public int idCheck(@RequestParam("userId") String user_id) {
		return sellerService.idCheck(user_id);
	}
	
	// 판매자 마이페이지 이동
	@GetMapping("/mypage")
	public String mypageView() {
		
		
		return "seller/mypage";
	}
	
	
	/*
	 * 판매자 정보 수정
	 */
	@PostMapping("/modify")
	public String modify(Seller seller, HttpSession session) {
		Seller theSeller = sellerService.modify(seller);
		session.setAttribute("seller", theSeller);
		
		return "redirect:/seller/";
	}
	
	
	/*
	 * 판매자 탈퇴
	 */
	@PostMapping("/quit")
	public String quit(Seller seller, HttpSession session) {
		session.invalidate();
		sellerService.quit(seller);
		
		return "redirect:/";
	}
	
	
	/*
	 * 판매자 메인화면 이동
	 */
	@RequestMapping("/")
	public String mainView(Model model,
						String searchKeyword,
						HttpSession session,
						@PageableDefault(page = 1) Pageable pageable) {
		
		Seller seller = (Seller) session.getAttribute("seller");
		Page<Product> productList = null;
		if(searchKeyword == null || searchKeyword.equals("")) {
			productList = sellerService.getProductList(pageable, seller);
		} else {
			productList = sellerService.search(seller, searchKeyword, pageable);
		}
		
		int naviSize = 10; // 페이지네이션 갯수
		int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / naviSize))) - 1) * naviSize + 1; // 1 11 21 31 ~~
	    int endPage = ((startPage + naviSize - 1) < productList.getTotalPages()) ? startPage + naviSize - 1 : productList.getTotalPages();
		
	    model.addAttribute("list", productList);
	    model.addAttribute("url", "/seller/");
	    model.addAttribute("productList", productList);
	    model.addAttribute("startPage", startPage);
	    model.addAttribute("endPage", endPage);
		
		return "seller/productList";
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
		
		return "seller/product";
	}
	
	/*
	 * 판매자 주문목록조회
	 */
	@GetMapping("/order")
	public String getOrderList(HttpSession session, Model model,
							@PageableDefault(page = 1) Pageable pageable) {
		Seller seller = (Seller) session.getAttribute("seller");
		Page<OrdersDetail> orderList = sellerService.getOrderList(seller, pageable);
		
		int naviSize = 10; // 페이지네이션 갯수
		int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / naviSize))) - 1) * naviSize + 1; // 1 11 21 31 ~~
	    int endPage = ((startPage + naviSize - 1) < orderList.getTotalPages()) ? startPage + naviSize - 1 : orderList.getTotalPages();
		
	    model.addAttribute("list", orderList);
	    model.addAttribute("url", "/seller/order");
	    model.addAttribute("orderList", orderList);
	    model.addAttribute("startPage", startPage);
	    model.addAttribute("endPage", endPage);		
	    System.out.println("orderList.size() = " + orderList.getSize());
		
		return "seller/orderList";
	}
	
	/*
	 * 판매자 qna목록조회
	 */
	@GetMapping("/qna")
	public String getQnAList(HttpSession session, Model model,
							@PageableDefault(page = 1) Pageable pageable) {
		Seller seller = (Seller) session.getAttribute("seller");
		Page<QnA> qnaList = sellerService.getQnAList(seller, pageable);
		
		int naviSize = 10; // 페이지네이션 갯수
		int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / naviSize))) - 1) * naviSize + 1; // 1 11 21 31 ~~
	    int endPage = ((startPage + naviSize - 1) < qnaList.getTotalPages()) ? startPage + naviSize - 1 : qnaList.getTotalPages();
		
	    model.addAttribute("list", qnaList);
	    model.addAttribute("url", "/seller/qna");
	    model.addAttribute("qnaList", qnaList);
	    model.addAttribute("startPage", startPage);
	    model.addAttribute("endPage", endPage);		
	    System.out.println("========= qnaList = " + qnaList);
		
		return "seller/qnaList";
	}
	
	/*
	 * 판매자 qna조회
	 */
	@GetMapping("/qna/{qno}")
	public String getQnA(@PathVariable int qno, Model model) {
		QnA qna = sellerService.getQnA(qno);
		System.out.println("qna = " + qna.getContent());
		model.addAttribute("qna", qna);
		
		return "seller/qna";
	}
	
	/*
	 * 판매자 상품등록
	 */
	@PostMapping("/product/insert")
	public String insertProduct(Product product, MultipartFile imageFile) throws Exception {
		product.setMdPickyn(0);
		sellerService.saveProduct(product, imageFile);
		
		return "redirect:/seller/";
	}
	
	/*
	 * 판매자 상품수정
	 */
	@PostMapping("/product/modify")
	public String modifyProduct(int pno, Product product, MultipartFile imageFile) throws Exception {
		System.out.println("product = " + product);
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












