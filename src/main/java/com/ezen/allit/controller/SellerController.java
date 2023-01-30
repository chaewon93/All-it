package com.ezen.allit.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;

import com.ezen.allit.domain.Product;
import com.ezen.allit.domain.Role;
import com.ezen.allit.domain.Seller;
import com.ezen.allit.service.ProductService;
import com.ezen.allit.service.SellerService;

@Controller
public class SellerController {
	@Autowired
	private SellerService sellerService;
	@Autowired
	private ProductService productService;
	
	// 홈 화면 이동
	@GetMapping({"", "/"})
	public String index() {
		
		return "index";
	}	
	
	// 판매자 입점신청 화면 이동
	@GetMapping("/seller/apply")
	public String applyView() {
		
		return "seller/apply";
	}
	
	// 판매자 로그인 화면 이동
	@GetMapping("/seller/login")
	public String loginView() {
		
		return "seller/login";
	}
		
	// 판매자 상품등록 화면 이동
	@GetMapping("/seller/insert")
	public String insertView() {
		
		return "seller/insert";
	}
	
	// 로그아웃
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate();
		
		return "index";
	}
	
	/*
	 * 판매자 로그인
	 */
	@PostMapping("/seller/login")
	public String login(String id, String pwd, HttpSession session, Model model) {
		Seller seller = sellerService.findByIdAndPwd(id, pwd);
		if(seller != null) {
			if(seller.getRole().equals(Role.ADMIN)) {
				session.setAttribute("seller", seller);
				return "admin/adminMain";
			}
			session.setAttribute("seller", seller);
			return "redirect:/seller/main";
		} else {
			
			return "seller/login";
		}
	}
	
	/*
	 *  판매자 입점신청
	 */
	@PostMapping("/seller/apply")
	public String apply(Seller seller) {
		seller.setRole(Role.TEMP);
		sellerService.saveSeller(seller);
		
		return "index";
	}
	
	/*
	 *  판매자 메인화면 이동
	 */
	@GetMapping("/seller/main")
	public String mainView(Model model, @PageableDefault(page = 1) Pageable pageable,
									String searchKeyword) {		
		Page<Product> productList = null;
		if(searchKeyword == null) {
			productList = productService.getProductList(pageable);
		} else {
			productList = productService.search(searchKeyword, pageable);
		}
		
		int naviSize = 10; // 페이지네이션 갯수
		int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / naviSize))) - 1) * naviSize + 1; // 1 11 21 31 ~~
	    int endPage = ((startPage + naviSize - 1) < productList.getTotalPages()) ? startPage + naviSize - 1 : productList.getTotalPages();
		
	    model.addAttribute("productList", productList);
	    model.addAttribute("startPage", startPage);
	    model.addAttribute("endPage", endPage);
		
		return "seller/main";
	}
	
	/*
	 *  상품 조회
	 */
	@GetMapping("/product/detail")
	public String getProduct(Product product, Model model,
							@PageableDefault(page = 1) Pageable pageable) {
		Product theProduct = productService.getProduct(product);
		model.addAttribute("product", theProduct);
		model.addAttribute("page", pageable.getPageNumber());
		
		return "product/detail";
	}
	
	/*
	 * 상품등록
	 */
	@PostMapping("/seller/insert")
	public String insertProduct(Product product, MultipartFile imageFile) throws Exception {
		product.setMdPickyn("n");
		productService.saveProduct(product, imageFile);
		
		return "redirect:/seller/main";
	}
	
	/*
	 * 상품수정
	 */
	@PostMapping("/seller/modify")
	public String modifyProduct(int pno, Product product, MultipartFile imageFile) throws Exception {
		productService.modifyProduct(pno, product, imageFile);
		
		return "redirect:/seller/main";
	}
	
}












