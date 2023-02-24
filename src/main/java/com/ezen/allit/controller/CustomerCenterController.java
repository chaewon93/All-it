package com.ezen.allit.controller;

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
import org.springframework.web.multipart.MultipartFile;

import com.ezen.allit.domain.CustomerCenter;
import com.ezen.allit.repository.CustomerCenterRepository;
import com.ezen.allit.service.CustomerCenterService;

@Controller
//@RequestMapping("/admin/")
public class CustomerCenterController {
	
	@Autowired
	CustomerCenterService cusService;
	
	@Autowired
	CustomerCenterRepository cusRepo;
	
	// 관리자 고객센터 리스트 조회
	@GetMapping("/admin/getCusto")
	public String getCusto(Model model, @PageableDefault(page = 1) Pageable pageable,
			@RequestParam(value= "searchKeyword", defaultValue = "") String searchKeyword) {
		
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
	
	// 관리자 고객센터 글 등록 페이지
	@GetMapping("/admin/insertCustoView")
	public String insertCustoView() {
		return "customerCenter/insertCusto";
	}
	
	// 관리가 고객센터 글 등록(이미지 파일 포함)
	@PostMapping("/admin/insertCusto")
	public String insertCusto(CustomerCenter cus, MultipartFile imageFile) throws Exception {
		
		cusService.insertCustomerCenter(cus, imageFile);
		
		return "redirect:getCusto";
	}
	
	// 고객센터 카테고리별 조회
	@GetMapping("/admin/findCusto")
	public String findCusto(Model model, @PageableDefault(page = 1) Pageable pageable,
			@RequestParam(value= "searchKeyword", defaultValue = "") String searchKeyword,
			@RequestParam(value= "cate", defaultValue = "") String cate) {

		Page<CustomerCenter> custoList = null;
		
		// cate가 전체이면 자주하는질문 을 모두 조회
		if(cate.equals("전체")) {
			cate = "자주하는질문";
		// cate가 모두 보기이면 고객센터 전체 조회
		}else if(cate.equals("모두 보기")) {
			return "redirect:getCusto";
		}
		// cate를 포함하는 카테고리 조회
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
	
	// 관리자 고객센터 글 상세보기
	@GetMapping("/admin/getCustoDetail")
	public String getCustoDetail(int cno, Model model) {
		CustomerCenter custo = cusRepo.findCustomerCenterByCno(cno);
		
		model.addAttribute("custo", custo);
		
		return "/customerCenter/custoDetail";
	}
	
	// 관리자 고객센터 글 수정(이미지 파일 포함)
	@PostMapping("/admin/updateCusto")
	public String updateCusto(CustomerCenter cus, MultipartFile imageFile) throws Exception {
		cusService.updateCusto(cus, imageFile);
		
		return "redirect:getCusto";
	}
	
	// 관리자 고객센터 글 삭제
	@GetMapping("/admin/deleteCusto")
	public String deleteCusto(int cno) {
		
		cusService.deleteCusto(cno);
		return "redirect:getCusto";
	}
	
	// 관리자 고객센터 글 메인화면 등록 여부(주로 이벤트.. 이미지파일 필수)
	@PostMapping("/admin/mainBanner")
	public String mainBanner(@RequestParam(value = "cno") int[] cno) {
		
		// 체크 리스트의 고객센터 글 등록여부 변경
		for(int i=0; i<cno.length; i++) {
			CustomerCenter customerCenter = cusRepo.findById(cno[i]).get();
			if(customerCenter.getPick().equals("0")) {
				customerCenter.setPick("1");
			}else {
				customerCenter.setPick("0");
			}
			cusRepo.save(customerCenter);
		}
		
		return "redirect:findCusto";
	}
	
	// 일반 회원들이 보는 고객센터 페이지
	@GetMapping("/customerCenter/userCustomerCenter")
	public String userCustomerCenterView(Model model, @PageableDefault(page = 1) Pageable pageable,
			@RequestParam(value= "searchKeyword", defaultValue = "") String searchKeyword,
			@RequestParam(value= "cate", defaultValue = "") String cate) {

			Page<CustomerCenter> custoList = null;
			
			if(cate.equals("모두 보기")) {
				custoList = cusService.getCustomercenter(pageable);
			}else {
				if(cate.equals("전체")) {
					cate = "자주하는질문";
				}
				custoList = cusService.findCustomerCenterByCategoryContaining(cate, pageable);
			}
				
			if(searchKeyword.equals("")) {
				System.out.println("검색 키워드 없음");
//				custoList = cusService.getCustomercenter(pageable);
			} else {
				System.out.printf("검색 키워드: [%s]\n", searchKeyword);
//				proList = adminService.searchByAdminPro(a, searchKeyword, pageable);
			}			
			
			int naviSize = 10; // 페이지네이션 갯수
			int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / naviSize))) - 1) * naviSize + 1; // 1 11 21 31 ~~
			int endPage = ((startPage + naviSize - 1) < custoList.getTotalPages()) ? startPage + naviSize - 1 : custoList.getTotalPages();

			System.out.println("-----------------------------------");
			System.out.println(custoList);
			System.out.println(custoList.getTotalElements());
			System.out.println(custoList.getTotalPages());
			System.out.println("-----------------------------------");
			
			model.addAttribute("list", custoList);
			model.addAttribute("url", "/customerCenter/userCustomerCenter");
			model.addAttribute("startPage", startPage);
			model.addAttribute("endPage", endPage);	
			model.addAttribute("cate", cate);
			
		return "customerCenter/userCustomerCenter";
	}

}
