package com.ezen.allit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.scheduling.annotation.Scheduled;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ezen.allit.domain.Coupon;
import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.Product;
import com.ezen.allit.domain.QnA;
import com.ezen.allit.domain.Reply;
import com.ezen.allit.domain.Role;
import com.ezen.allit.domain.Seller;
import com.ezen.allit.repository.CouponRepository;
import com.ezen.allit.repository.MemberRepository;
import com.ezen.allit.repository.ProductRepository;
import com.ezen.allit.repository.QnARepository;
import com.ezen.allit.repository.SellerRepository;
import com.ezen.allit.service.AdminService;
import com.ezen.allit.service.CouponService;

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
	
	@Autowired
	private MemberRepository memRepo;
	
	@Autowired
	private CouponService couponService;
	
	@Autowired
	private CouponRepository couponRepo;
	
	// 관리자 고객 조회
	@GetMapping("/getMemberList")
	public String getMemberList(Model model, @PageableDefault(page = 1) Pageable pageable,
			@RequestParam(value= "searchKeyword", defaultValue = "") String searchKeyword,
			@RequestParam(value= "a", defaultValue = "0") int a) {
		
		Page<Member> memberList = null;
		if(searchKeyword.equals("")) {
			System.out.println("검색 키워드 없음");
			memberList = adminService.getMemberList(pageable);
		} else {
			System.out.printf("검색 키워드: [%s]\n", searchKeyword);
			memberList = adminService.searchByAdminMem(searchKeyword, pageable);
		}

		int naviSize = 10; // 페이지네이션 갯수
		int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / naviSize))) - 1) * naviSize + 1; // 1 11 21 31 ~~
		int endPage = ((startPage + naviSize - 1) < memberList.getTotalPages()) ? startPage + naviSize - 1 : memberList.getTotalPages();
				
		model.addAttribute("list", memberList);
		model.addAttribute("url", "/admin/getMemberList");
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);	

		return "/admin/getMemberList";
	}
	
	// 관리자 페이지 
	@GetMapping("adminMain")
	public String adminMain() {
		return "/admin/adminMain";
	}
	
	// 관리자 QnA 조회
	// a로 전체, 미답변, 답변 QnA 구분 조회
	@RequestMapping("getQnAList")
	public String getQnAList(Model model, @PageableDefault(page = 1) Pageable pageable,
			@RequestParam(value= "searchKeyword", defaultValue = "") String searchKeyword,
			@RequestParam(value= "a", defaultValue = "-1") int status) {
		
		Page<QnA> qnaList = null;
		
		if(searchKeyword.equals("")) {
			System.out.println("검색 키워드 없음");
			if(status == -1) {
				qnaList = adminService.getQnAList(pageable);
			}else if(status == 0 || status == 1) {
				qnaList = adminService.findQnAByStatus(status, pageable);
			}
		} else {
			System.out.printf("검색 키워드: [%s]\n", searchKeyword);
//			qnaList = adminService.searchByAdminQna(searchKeyword, pageable);
		}		
		
		System.out.println("-----------------------------------");
		System.out.println(qnaList);
		System.out.println(qnaList.getTotalElements());
		System.out.println(qnaList.getTotalPages());
		System.out.println("-----------------------------------");
		
		int naviSize = 10; // 페이지네이션 갯수
		int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / naviSize))) - 1) * naviSize + 1; // 1 11 21 31 ~~
		int endPage = ((startPage + naviSize - 1) < qnaList.getTotalPages()) ? startPage + naviSize - 1 : qnaList.getTotalPages();

//		model.addAttribute("proList", proList);
		model.addAttribute("list", qnaList);
		model.addAttribute("url", "/admin/getQnAList");
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);	
		model.addAttribute("a", status);
		
		return "/admin/getQnAList";
	}
	
/* // 관리자 미답변 QnA 조회
	@RequestMapping("getNoQnAList")
	public String getNoQnAList(Model model, @PageableDefault(page = 1) Pageable pageable,
			@RequestParam(value= "searchKeyword", defaultValue = "") String searchKeyword,
			@RequestParam(value= "a", defaultValue = "-1") int status) {
		
		Page<QnA> qnaList = null;

		if(searchKeyword.equals("")) {
			System.out.println("검색 키워드 없음");
			qnaList = adminService.findQnAByStatus(status, pageable);

		} else {
			System.out.printf("검색 키워드: [%s]\n", searchKeyword);
//			qnaList = adminService.searchByAdminQnaStatus(status, searchKeyword, pageable);
		}		

		System.out.println("-----------------------------------");
		System.out.println(qnaList);
		System.out.println(qnaList.getTotalElements());
		System.out.println(qnaList.getTotalPages());
		System.out.println("-----------------------------------");
		
		int naviSize = 10; // 페이지네이션 갯수
		int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / naviSize))) - 1) * naviSize + 1; // 1 11 21 31 ~~
		int endPage = ((startPage + naviSize - 1) < qnaList.getTotalPages()) ? startPage + naviSize - 1 : qnaList.getTotalPages();

//		model.addAttribute("proList", proList);
		model.addAttribute("list", qnaList);
		model.addAttribute("url", "/admin/getNoQnAList");
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);	
		model.addAttribute("a", status);
		
		return "/admin/getQnAList";
	}*/
	
	// 관리자 QnA 카테고리 별 조회
	// cate 로 카테고리를 받아서 분류
	@GetMapping("findQnA")
	public String findQnAByCategory(Model model, @PageableDefault(page = 1) Pageable pageable,
						@RequestParam(value= "searchKeyword", defaultValue = "") String searchKeyword,
						@RequestParam(value= "cate", defaultValue = "") String cate) {

		System.out.println("카테고리 번호 : "+cate);
		
		Page<QnA> qnaList = null;
		
		// 모두 보기이면 전체 조회이므로 첫 화면 다시 보여줌
		if(cate.equals("모두 보기")) {
			return "redirect:getQnAList";
		}
		// 카테고리컬럼에서 cate를 포함하는 QnA 조회
		qnaList = adminService.findQnAByCategoryContaining(cate, pageable);

		int naviSize = 10; // 페이지네이션 갯수
		int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / naviSize))) - 1) * naviSize + 1; // 1 11 21 31 ~~
		int endPage = ((startPage + naviSize - 1) < qnaList.getTotalPages()) ? startPage + naviSize - 1 : qnaList.getTotalPages();

		model.addAttribute("list", qnaList);
		model.addAttribute("url", "/admin/findQnA");
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);	
		model.addAttribute("a", cate);
		
		return "/admin/getQnAList";
	}
	
	// 관리자 QnA 상세보기
	@RequestMapping("getQnADetail")
	public String getQnADetail(Model model, int qno) {
		
		QnA qna = qnaRepo.findQnAByQno(qno);

		model.addAttribute("qna", qna);
		return "/admin/QnADetail";
	}
	
	//관리자 QnA 답변 등록
	@PostMapping("insertReply")
	public String insertReply(String qno, Reply rep, Model model) {
		
		System.out.println(qno);
		
		QnA temp = new QnA();
		temp.setQno(Integer.parseInt(qno));

		adminService.insertReply(temp, rep);

		return "redirect:/admin/getQnAList";
	}
	
	// 관리자 QnA 답변 수정
	@PostMapping("updateReply")
	public String updateReply(Reply rep) {

		adminService.updateReply(rep);
				
		return "redirect:/admin/getQnAList";
	}

	// 관리자 QnA 답변 삭제
	@GetMapping("deleteReply")
	public String deleteReply(int qno) {
		
		adminService.deleteReply(qno);
		
		return "redirect:/admin/getQnAList";
	}
	
	// 관리자 판매,관리,판매대기자 조회
	// a로 조회 조건을 분류해서 조회
	@GetMapping("findSellerList")
	public String findSellerList(Model model, @PageableDefault(page = 1) Pageable pageable,
			@RequestParam(value= "searchKeyword", defaultValue = "") String searchKeyword,
			@RequestParam(value= "a", defaultValue = "0") int a) {
		int b = 0;
		Page<Seller> sellerList = null;
		if(a==0) {	// 관리자,판매자, 판매대기자 전부 조회
			sellerList = adminService.findAllSeller(pageable);
		}else if(a==1) {	// 관리자 조회
			sellerList = adminService.findSellerByRole(Role.ADMIN, pageable);
		}else if(a==2) {	// 판매자, 판매대기자 조회
			sellerList = adminService.findSellerByRoleNot(Role.ADMIN, pageable);
			b = 1;
		}else if(a==3) {	// 판매자 조회
			sellerList = adminService.findSellerByRole(Role.SELLER, pageable);
			b = 1;
		}else if(a==4) {	// 판매대기자 조회
			sellerList = adminService.findSellerByRole(Role.TEMP, pageable);
			b = 1;
		}
		
		int naviSize = 10; // 페이지네이션 갯수
		int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / naviSize))) - 1) * naviSize + 1; // 1 11 21 31 ~~
		int endPage = ((startPage + naviSize - 1) < sellerList.getTotalPages()) ? startPage + naviSize - 1 : sellerList.getTotalPages();

		model.addAttribute("list", sellerList);
		model.addAttribute("url", "/admin/findSellerList");
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);	
		model.addAttribute("a", a);
		model.addAttribute("b", b);
		
		return "/admin/findSellerList";		
	}
	
	// 관리자 판매대기자를 판매자로 등록
	@PostMapping("changeSeller")
	public String changeSeller(@RequestParam(value = "sellerId") String[] sellerId, RedirectAttributes re) {
		// model은 주로 페이지(view)로 보낼 때 사용...
		// RedirectAttributes은 컨트롤러로 보낼 때 사용... a 를 함께 보내줌
		
		// 체크된 판매자 리스트에서 SELLER이면 TEMP로, TEMP면 SELLER로 변경
		for(int i=0; i<sellerId.length; i++) {
			
			Seller seller = sellerRepo.findById(sellerId[i]).get();
			if(seller.getRole().equals(Role.TEMP)) {
				seller.setRole(Role.SELLER);
			}else if(seller.getRole().equals(Role.SELLER)) {
				seller.setRole(Role.TEMP);
			}
			sellerRepo.save(seller);
		}
		re.addAttribute("a", 2);
//		String pass = "redirect:/admin/findSellerList?a=2"; 
		String pass = "redirect:/admin/findSellerList"; 
		return pass;
	}
	
	// 관리자 판매자 삭제
	@PostMapping("deleteSeller")
	public String deleteSeller(@RequestParam(value = "sellerId") String[] sellerId, RedirectAttributes re) {
		// 체크된 판매자 리스트에서 삭제
		for(int i=0; i<sellerId.length; i++) {
			
			Seller seller = sellerRepo.findById(sellerId[i]).get();
			
			sellerRepo.delete(seller);
		}
		re.addAttribute("a", 0);
		return "redirect:/admin/findSellerList";
	}
	
	// 관리자 판매상품(대기 및 등록) 조회 검색
	// a가 0이면 대기상품, 1이면 등록상품
	@GetMapping("findAdminProduct")
	public String findAdminProduct(Model model, @PageableDefault(page = 1) Pageable pageable,
								@RequestParam(value= "searchKeyword", defaultValue = "") String searchKeyword,
								@RequestParam(value= "a", defaultValue = "0") int status) {

		Page<Product> proList = null;
		if(searchKeyword.equals("")) {
			System.out.println("검색 키워드 없음");
			proList = adminService.findProductByStatus(status, pageable);
		} else {
			System.out.printf("검색 키워드: [%s]\n", searchKeyword);
//			proList = adminService.searchByAdminPro(a, searchKeyword, pageable);
		}
		
		int naviSize = 10; // 페이지네이션 갯수
		int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / naviSize))) - 1) * naviSize + 1; // 1 11 21 31 ~~
		int endPage = ((startPage + naviSize - 1) < proList.getTotalPages()) ? startPage + naviSize - 1 : proList.getTotalPages();
		
//		model.addAttribute("proList", proList);
		model.addAttribute("list", proList);
		model.addAttribute("url", "/admin/findAdminProduct");
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);	
		model.addAttribute("a", status);
		
		return "admin/findAdminProduct";
	}
	
	// 관리자 상품 등록 상태 변경
	@PostMapping("changeProStatus")
	public String changeProStatus(@RequestParam(value = "pno") int[] pno) {
		
		// 체크 리스트를 받아서 상태 변경
		for(int i=0; i<pno.length; i++) {
			
			Product product = proRepo.findById(pno[i]).get();
			if(product.getStatus() == 0) {
				product.setStatus(1);
			}else if(product.getStatus() == 1) {
				product.setStatus(0);
			}
			proRepo.save(product);
		}
		String pass = "redirect:findAdminProduct"; 
		return pass;
	}	
	
	// 관리자 등록된 상품 MDPICK 선정 여부 변경
	@PostMapping("changeProMDPick")
	public String changeProMDPick(@RequestParam(value = "pno") int[] pno) {
		
		// 체크 리스트에서 MDPCIK 상태 변경
		for(int i=0; i<pno.length; i++) {
			
			Product product = proRepo.findById(pno[i]).get();
			if(product.getMdPickyn() == 0) {
				product.setMdPickyn(1);
			}else {
				product.setMdPickyn(0);
			}
			proRepo.save(product);
		}
		String pass = "redirect:findAdminProduct?a=1"; 
		return pass;
	}	
	
	// 관리자 상품 삭제
	@PostMapping("deletePro")
	public String deletePro(@RequestParam(value = "pno") int[] pno) {
	
		for(int i=0; i<pno.length; i++) {
			
			Product product = proRepo.findById(pno[i]).get();
			
			proRepo.delete(product);
		}
		
		return "redirect:findAdminProduct";
	}
	
	// 쿠폰 목록 조회
	@GetMapping("couponList")
	public String couponList(Model model, @PageableDefault(page = 1) Pageable pageable,
			@RequestParam(value= "searchKeyword", defaultValue = "") String searchKeyword) {
		
		Page<Coupon> couponList = null;
		if(searchKeyword.equals("")) {
			System.out.println("검색 키워드 없음");
			couponList = couponService.findCouponList(pageable);
		} else {
			System.out.printf("검색 키워드: [%s]\n", searchKeyword);
		}
		
		int naviSize = 10; // 페이지네이션 갯수
		int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / naviSize))) - 1) * naviSize + 1; // 1 11 21 31 ~~
		int endPage = ((startPage + naviSize - 1) < couponList.getTotalPages()) ? startPage + naviSize - 1 : couponList.getTotalPages();
		
		model.addAttribute("list", couponList);
		model.addAttribute("url", "/admin/couponList");
		model.addAttribute("startPage", startPage);
		model.addAttribute("endPage", endPage);	
		
		return "admin/couponList";
	}
	
	// 쿠폰 창 열기(생성과 쿠폰 상세보기 같이 함)
	@GetMapping("createCoupon")
	public String createCouponView(Model model, @RequestParam(value="couno", defaultValue = "0") int couno) {
		if(couno == 0) {
			return "admin/createCoupon";
		}else {
			Coupon coupon = couponRepo.findById(couno).get();
			model.addAttribute("coupon", coupon);
			return "admin/createCoupon";
		}		
	}
  
	// 쿠폰 생성과 쿠폰 수정
	@PostMapping("createCoupon")
	public String createCoupon(Coupon coupon) {

		// 쿠폰 창에서 생성이면 couId가 없고 수정이면 couId가 있음.. 이거로 구분
		if(couponRepo.findById(coupon.getCouId()).isPresent()) {
			System.out.println("=========================쿠폰이 이미 있습니다 수정입니다.===========================");
			System.out.println(coupon.getCouId());
			couponService.updateCoupon(coupon);
		}else {
			System.out.println("=========================쿠폰이 없습니다 생성합니다.===========================");
			System.out.println(coupon.getCouId());
			couponService.createCoupon(coupon);
		}
		
		return "redirect:couponList";
	}
	
	// 쿠폰 삭제
	@RequestMapping("deleteCoupon")
	public String deleteCoupon(Coupon coupon) {
		couponRepo.delete(coupon);		
		return "redirect:couponList";
	}
	
	// 매달 1일 00:00 이 되면 모든 멤버들에게 특정 쿠폰을 보내줌.. 
	@Scheduled(cron="0 0 0 1 * *")
	public void checkFirst() {
		
		List<Member> memList = memRepo.findAll();
		
		// 특정 쿠폰 지금은 couId가 3인 쿠폰을 보내줌 
		for(Member member : memList) {
			couponService.insertMemCoupon(member, 81);
		}		
	}
}

