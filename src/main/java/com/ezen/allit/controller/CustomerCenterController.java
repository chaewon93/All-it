package com.ezen.allit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.ezen.allit.domain.CustomerCenter;
import com.ezen.allit.domain.Product;
import com.ezen.allit.domain.QnA;
import com.ezen.allit.repository.CustomerCenterRepository;
import com.ezen.allit.service.CustomerCenterService;

@Controller
@RequestMapping("/admin/")
public class CustomerCenterController {
	
	@Autowired
	CustomerCenterService cusService;
	
	@Autowired
	CustomerCenterRepository cusRepo;
	
	@RequestMapping("/getCusto")
	public String getCusto(Model model, @PageableDefault(page = 1) Pageable pageable,
			@RequestParam(value= "searchKeyword", defaultValue = "") String searchKeyword,
			@RequestParam(value= "a", defaultValue = "") String a) {
		
		Page<CustomerCenter> custoList = null;
		if(searchKeyword.equals("")) {
			System.out.println("검색 키워드 없음");
			custoList = cusService.getCustomercenter(pageable);
		} else {
			System.out.printf("검색 키워드: [%s]\n", searchKeyword);
//			proList = adminService.searchByAdminPro(a, searchKeyword, pageable);
		}
		
		int naviSize = 10; // 페이지네이션 갯수
		int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / naviSize))) - 1) * naviSize + 1; // 1 11 21 31 ~~
		int endPage = ((startPage + naviSize - 1) < custoList.getTotalPages()) ? startPage + naviSize - 1 : custoList.getTotalPages();

		model.addAttribute("list", custoList);
		model.addAttribute("url", "/admin/getCusto");
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);	

		return "customerCenter/customerCenter";
	}
	
	@RequestMapping("/insertCustoView")
	public String insertCustoView() {
		return "customerCenter/insertCusto";
	}
	
	@RequestMapping("/insertCusto")
	public String insertCusto(CustomerCenter cus) {
		
		cusService.insertCustomerCenter(cus);
		
		return "redirect:getCusto";
	}
	
	@RequestMapping("/findCusto")
	public String findCusto(Model model, @PageableDefault(page = 1) Pageable pageable,
			@RequestParam(value= "searchKeyword", defaultValue = "") String searchKeyword,
			@RequestParam(value= "cate", defaultValue = "") String cate) {

		Page<CustomerCenter> custoList = null;
		
		if(cate.equals("전체")) {
			cate = "자주하는질문";
		}else if(cate.equals("모두 보기")) {
			return "redirect:getCusto";
		}
		custoList = cusService.findCustomerCenterByCategoryContaining(cate, pageable);

		int naviSize = 10; // 페이지네이션 갯수
		int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / naviSize))) - 1) * naviSize + 1; // 1 11 21 31 ~~
		int endPage = ((startPage + naviSize - 1) < custoList.getTotalPages()) ? startPage + naviSize - 1 : custoList.getTotalPages();

		model.addAttribute("list", custoList);
		model.addAttribute("url", "/admin/findCusto");
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);	
		model.addAttribute("a", cate);
		
		return "customerCenter/customerCenter";
	}
	
	@GetMapping("/getCustoDetail")
	public String getCustoDetail(int cno, Model model) {
		CustomerCenter custo = cusRepo.findCustomerCenterByCno(cno);
		
		model.addAttribute("custo", custo);
		
		return "/customerCenter/custoDetail";
	}
	
	@PostMapping("/updateCusto")
	public String updateCusto(CustomerCenter cus) {
		
		cusService.updateCusto(cus);
		
		return "redirect:getCusto";
	}
	
	@GetMapping("/deleteCusto")
	public String deleteCusto(int cno) {
		
		cusService.deleteCusto(cno);
		return "redirect:getCusto";
	}

}
