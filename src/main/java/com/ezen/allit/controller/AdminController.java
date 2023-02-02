package com.ezen.allit.controller;

import java.util.*;

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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.Product;
import com.ezen.allit.domain.QnA;
import com.ezen.allit.domain.Reply;
import com.ezen.allit.domain.Role;
import com.ezen.allit.domain.Seller;
import com.ezen.allit.repository.ProductRepository;
import com.ezen.allit.repository.QnARepository;
import com.ezen.allit.repository.SellerRepository;
import com.ezen.allit.service.AdminService;

@Controller
@RequestMapping("/admin/")
public class AdminController {
	
	@Autowired
	private AdminService adminService;
	
	@Autowired
	private QnARepository qnaRepo;
	
	@Autowired
	private SellerRepository sellerRepo;
	
	@Autowired
	private ProductRepository proRepo;
	
	@GetMapping("/getMemberList")
	public String getMemberList(Model model) {

		List<Member> memberList = adminService.getMemberList();

		model.addAttribute("memberList", memberList);
		return "/admin/getMemberList";
	}
	
	@GetMapping("adminMain")
	public String adminMain() {
		return "/admin/adminMain";
	}
	
	@RequestMapping("getQnAList")
	public String getQnAList(Model model) {
		
		List<QnA> qnaList = adminService.getQnAList();
		
		model.addAttribute("qnaList", qnaList);
		
		return "/admin/getQnAList";
	}
	
	@RequestMapping("getNoQnAList")
	public String getNoQnAList(Model model, String status) {
		
		List<QnA> noQnaList = qnaRepo.findQnAByStatus("0");
		
		System.out.println("---------------------------- No QnA List");
		System.out.println("noQnaList : "+noQnaList);
		System.out.println("---------------------------- No QnA List1");
		
		if(noQnaList.isEmpty()) {
			noQnaList = null;
		}
		model.addAttribute("qnaList", noQnaList);
		
		return "/admin/getQnAList";
	}
	
	@RequestMapping("getQnADetail")
	public String getQnADetail(Model model, int qno) {
		
		QnA qna = qnaRepo.findQnAByQno(qno);
		
		System.out.println("----------------------------- detail QnA");
		System.out.println(qna);
		System.out.println("----------------------------- detail QnA");
		
		model.addAttribute("qna", qna);
		return "/admin/QnADetail";
	}
	
	@PostMapping("insertReply")
	public String insertReply(String qno, Reply rep, Model model) {
		
		System.out.println(qno);
		
		QnA temp = new QnA();
		temp.setQno(Integer.parseInt(qno));

		System.out.println("----------------");
		System.out.println(temp);
		System.out.println("----------------");
		
		adminService.insertReply(temp, rep);
		
		QnA qna = qnaRepo.findQnAByQno(Integer.parseInt(qno));
		
		System.out.println("---------------- insert QnA");
		System.out.println(qna);
		System.out.println("---------------- insert QnA");
		
		return "redirect:/admin/getQnAList";
	}
	
	@PostMapping("updateReply")
	public String updateReply(Reply rep) {
		
		System.out.println("---------------- update Reply");
		System.out.println(rep);
		System.out.println("---------------- update Reply");
		
		adminService.updateReply(rep);
				
		return "redirect:/admin/getQnAList";
	}

	@GetMapping("deleteReply")
	public String deleteReply(int qno) {
		
		adminService.deleteReply(qno);
		
		return "redirect:/admin/getQnAList";
	}
	
	@GetMapping("findSellerList")
	public String findSellerList(int a, Model model, Role role) {
		int b = 0;
		List<Seller> sellerList = new ArrayList<>();
		if(a==0) {	// 관리자,판매자, 판매대기자 전부 조회
			sellerList = sellerRepo.findAll();
		}else if(a==1) {	// 관리자 조회
			sellerList = sellerRepo.findSellerByRole(role.ADMIN);
		}else if(a==2) {	// 판매자, 판매대기자 조회
			sellerList = sellerRepo.findSellerByRoleNot(role.ADMIN);
			a = 6;
			b = 1;
		}else if(a==3) {	// 판매자 조회
			sellerList = sellerRepo.findSellerByRole(role.SELLER);
		}else if(a==4) {	// 판매대기자 조회
			sellerList = sellerRepo.findSellerByRole(role.TEMP);
			a = 5;
			b = 1;
		}
		model.addAttribute("sellerList", sellerList);
		model.addAttribute("a", a);
		model.addAttribute("b", b);
		return "/admin/findSellerList";
	}
	
	@PostMapping("regSeller")
	public String regSeller(@RequestParam(value = "sellerId") String[] sellerId, RedirectAttributes re) {
		// model은 주로 페이지(view)로 보낼 때 사용...
		// RedirectAttributes은 컨트롤러로 보낼 때 사용...
		
		for(int i=0; i<sellerId.length; i++) {
			
			Seller seller = sellerRepo.findById(sellerId[i]).get();
			if(seller.getRole().equals(Role.TEMP)) {
				seller.setRole(Role.SELLER);
			}
			sellerRepo.save(seller);
		}
		re.addAttribute("a", 0);
		String pass = "redirect:/admin/findSellerList"; 
		return pass;
	}
	
	@PostMapping("deleteSeller")
	public String deleteSeller(@RequestParam(value = "sellerId") String[] sellerId, RedirectAttributes re) {
	
		for(int i=0; i<sellerId.length; i++) {
			
			Seller seller = sellerRepo.findById(sellerId[i]).get();
			
			sellerRepo.delete(seller);
		}
		re.addAttribute("a", 0);
		return "redirect:/admin/findSellerList";
	}
	
	@GetMapping("findAdminProduct")
	public String findAdminProduct(Model model, @PageableDefault(page = 1) Pageable pageable) {
		
//		Page<Product> proList = proRepo.findProductByStatus(0, pageable);
		Page<Product> proList = adminService.findProductByStatus(0, pageable);
		
		model.addAttribute("proList", proList);
		
		return "admin/findAdminProduct";
	}

}
