package com.ezen.allit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ezen.allit.domain.Product;
import com.ezen.allit.service.ProductService;

@Controller
@RequestMapping("/product/")
public class ProductController {
	
	@Autowired
	private ProductService productService;

	/** 신상품 조회 */
	@GetMapping("/newItem")
	public String newItemList(Model model, @PageableDefault(page = 1) Pageable pageable,
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
		
		return "product/newItem";
	}
	
	/** 베스트상품 조회 */
	
	/** 상품 상세조회 */
	@GetMapping("/detail/{pno}")
	public String getProduct(@PathVariable int pno, Model model,
							@PageableDefault(page = 1) Pageable pageable) {
		Product theProduct = productService.getProduct(pno);
		model.addAttribute("product", theProduct);
		model.addAttribute("page", pageable.getPageNumber());
		
		return "product/detail";
	}
	
}
