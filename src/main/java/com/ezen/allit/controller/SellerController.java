package com.ezen.allit.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.ezen.allit.config.auth.PrincipalDetailSeller;
import com.ezen.allit.domain.OrdersDetail;
import com.ezen.allit.domain.Product;
import com.ezen.allit.domain.QnA;
import com.ezen.allit.domain.Review;
import com.ezen.allit.domain.Role;
import com.ezen.allit.domain.Seller;
import com.ezen.allit.dto.ResponseDto;
import com.ezen.allit.dto.SearchDto;
import com.ezen.allit.service.SellerService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {
	private final SellerService sellerService;
	private final AuthenticationManager authenticationManager;
		
	/** 상품등록
	 * @author 정동욱
	 * @return 상품등록 페이지로 이동
	 */
	@GetMapping("/insert")
	public String insertView() {
		
		return "seller/insert";
	}
	
	/** 로그아웃
	 * @author 정동욱
	 * @param  session 로그인 후 인증된 객체
	 * @return 메인 페이지로 이동
	 */
//	@GetMapping("/logout")
//	public String logout(HttpSession session) {
//		session.invalidate();
//		
//		return "redirect:/";
//	}
	
	/** 판매자 로그인
	 * @author 정동욱
	 * @param seller  로그인 페이지에서 넘어온 값을 담은 객체
	 * @param session 로그인 성공시 저장할 세션 객체
	 * @return 		  로그인 성공시 Role에 따라 관리자 메인, 판매자 메인 페이지로 이동, 실패시 로그인 페이지 이동(새로고침)
	 */
//	@PostMapping("/login")
//	public String login(Seller seller, HttpSession session) {
//		Seller theSeller = sellerService.findByIdAndPwd(seller.getId(), seller.getPwd());
//		if(theSeller != null) {
//			if(theSeller.getRole().equals(Role.ADMIN)) {
//				session.setAttribute("admin", theSeller);
//				
//				return "admin/adminMain";
//			}
//			if(theSeller.getRole().equals(Role.SELLER)) {
//				session.setAttribute("seller", theSeller);
//				return "redirect:/seller/";
//			} else {
//				
//				return "seller/loginError";
//			}
//		} else {
//			
//			return "seller/login";
//		}
//	}
	
	/** 판매자 마이페이지 이동
	 * @author 정동욱, 임채원
	 * @param model 	판매자 마이페이지에 넘겨줄 정보 저장
	 * @param principal 현재 로그인을 통해 인증된 객체
	 * @return 			Role에 따라 관리자 마이페이지, 판매자 메인페이지로 이동 
	 */
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
		
		if(seller.getRole().equals(Role.ADMIN)) {
			return "seller/mypage";
		} else {
			return "seller/mypageCheck";
		}

	}
	
	/** 판매자 정보 수정 전 비밀번호 확인 
	 * @author 정동욱
	 * @param seller 	비밀번호 확인 페이지에서 넘어온 값을 담을 객체
	 * @param model 	비밀번호 확인 후 주소정보를 담을 객체
	 * @param response  비밀번호 확인 실패 시 alert창을 생성하기 위한 객체
	 * @param principal 현재 로그인을 통해 인증된 객체
	 * @return 			비밀번호 인증 성공 시 마이페이지로 이동, 실패 시 알림 후 비밀번호 확인 페이지로 이동(새로고침)
	 * @throws IOException PrintWriter 객체 사용으로 스트림 발생. 스트림 예외
	 */
	@PostMapping("/infoCheck")
	public String infoCheck(Seller seller, Model model, HttpServletResponse response,
						@AuthenticationPrincipal PrincipalDetailSeller principal) throws IOException {
		
		boolean result = sellerService.pwdCheck(seller);
		Seller session = principal.getSeller();

		if(result) {
			String fullAddr = session.getAddress();
			if(fullAddr != null) {
				String[] addr = fullAddr.split(",");
				model.addAttribute("addr", addr);
			}
			
			return "seller/mypage";
		} else {
			response.setContentType("text/html; charset=UTF-8");
			PrintWriter out = response.getWriter();

			out.println("<script>alert('비밀번호가 틀렸습니다.'); location.href='/seller/mypage'</script>");
			out.flush();
			out.close();

			return null;
		}
	}
	
	/** 판매자 비밀번호변경 
	 * @author 임채원
	 * @param model  비밀번호변경 성공 후 변경된 값을 다시 담을 객체
	 * @param seller 비밃번호변경 페이지에서 넘어온 값을 받을 객체
	 * @return 	  	 ajax 사용. 페이지 이동이 아닌 매서드 처리가 성공했음을 알림
	 */
	@ResponseBody
	@PutMapping("/Pwdmodify/{pwd}")
	public ResponseDto<Integer> modifyPwd(Model model,
										@RequestBody Seller seller) {

		Seller modify_seller = sellerService.modifySellerPwd(seller);
		model.addAttribute("user", modify_seller);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);		
	}
	
	/** 판매자 정보 수정
	 * @author 정동욱
	 * @param seller 정보수정 페이지에서 넘어온 값을 받을 객체
	 * @return		 판매자 메인 페이지로 이동
	 */
	@PostMapping("/modify")
	public String modify(Seller seller) {
		sellerService.modify(seller);
		
		/* 수정한 정보로 세션 재주입 */
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(seller.getId(), seller.getPwd()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return "redirect:/seller/";
	}
	
	/** 판매자 탈퇴 
	 * @author 임채원
	 * @param seller 회원탈퇴 페이지에서 넘어온 값으로 DB의 패스워드와 비교할 객체
	 * @return		 비밀번호가 맞으면 1, 틀리면 0을 반환
	 */
	@ResponseBody
	@DeleteMapping("/delete/{id}")
	public int deleteMember(@RequestBody Seller seller) {
		boolean match = sellerService.pwdCheck(seller);

		if(match) {
			sellerService.quit(seller);
			SecurityContextHolder.clearContext();			
			return 1;		
		} else {
			return 0;
		}
	}
	
	/** 판매자 메인화면 이동
	 * @author 정동욱
	 * @param model 		  판매자 메인 페이지에 넘겨줄 정보 저장
	 * @param searchCondition 검색조건으로 넘어온 값을 받을 객체 
	 * @param searchKeyword   검색어로 넘어온 값을 받을 객체
	 * @param pageable 		  판매자 메인 페이지 페이징 처리를 위해 필요한 객체
	 * @param principal 	  현재 로그인을 통해 인증된 객체
	 * @return 				  판매자 메인 페이지로 이동
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
	    if(productList.getTotalElements() == 0) model.addAttribute("size", 0);
		
		return "seller/productList";
	}
	
	/** 판매자 미등록상품목록 조회
	 * @author 정동욱
	 * @param model 	판맴자 미등록 상품목록 페이지에 넘겨줄 정보 저장
	 * @param pageable 	판매자 미등록 상품목록 페이지 페이징 처리를 위해 필요한 객체
	 * @param principal 현재 로그인을 통해 인증된 객체
	 * @return 			판매자 미등록 상품목록 페이지로 이동
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
	    if(productList.getTotalElements() == 0) model.addAttribute("size", 0);
		
		return "seller/unRegisteredProductList";
	}
	
	/** 판매자 주문목록조회
	 * @author 정동욱
	 * @param model 		판매자 주문목록 페이지에 넘겨줄 정보 저장
	 * @param searchKeyword 검색어로 넘어온 값을 받을 객체
	 * @param pageable 		판매자 주문목록 페이지 페이징 처리를 위해 필요한 객체
	 * @param principal 	현재 로그인을 통해 인증된 객체
	 * @return 				판매자 주문목록 페이지로 이동
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
	
	/** 판매자 qna목록조회
	 * @author 정동욱
	 * @param model 		판매자 문의목록 페이지에 넘겨줄 정보 저장
	 * @param searchKeyword 검색어로 넘어온 값을 받을 객체
	 * @param pageable 		판매자 문의목록 페이지 페이징 처리를 위해 필요한 객체
	 * @param principal 	현재 로그인을 통해 인증된 객체
	 * @return 				판매자 문의목록 페이지로 이동
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
	
	/** 판매자 상품조회
	 * @author 정동욱
	 * @param model    판매자 상품 페이지에 넘겨줄 정보 저장
	 * @param pno      판매자 상품목록 페이지에서 넘겨준 상품번호
	 * @param pageable 판매자 상품조회 후 페이징 유지를 위해 필요한 객체
	 * @return 		   판매자 상품 페이지로 이동
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
	
	/** 판매자 qna조회
	 * @author 정동욱
	 * @param model    판매자 문의답변 페이지에 넘겨줄 정보 저장
	 * @param qno      판매자 문의목록 페이지에서 넘겨준 상품번호
	 * @param pageable 판매자 문의조회 후 페이징 유지를 위해 필요한 객체
	 * @return 		   판매자 문의답변 페이지로 이동
	 */
	@GetMapping("/qna/{qno}")
	public String getQnA(Model model,
						@PathVariable int qno,
						@PageableDefault(page = 1) Pageable pageable) {
		
		QnA qna = sellerService.getQnA(qno);
		model.addAttribute("qna", qna);
		model.addAttribute("page", pageable.getPageNumber());
		
		return "seller/qna";
	}
	
	/** 판매자 상품등록
	 * @author 정동욱
	 * @param product   판매자 상품등록 페이지에서 넘어온 값을 받을 객체
	 * @param imagaFile 판매자 상품등록 페이지에서 넘어온 file객체를 받을 객체
	 * @return 		    판매자 메인 페이지로 이동
	 */
	@PostMapping("/product/insert")
	public String insertProduct(Product product, MultipartFile imageFile) throws Exception {
		sellerService.saveProduct(product, imageFile);
		
		return "redirect:/seller/";
	}
	
	/** 판매자 상품수정
	 * @author 정동욱
	 * @param pno		판매자 상품수정 페이지에서 넘어온 값을 받을 객체
	 * @param product   판매자 상품수정 페이지에서 넘어온 값을 받을 객체
	 * @param imagaFile 판매자 상품수정 페이지에서 넘어온 file객체를 받을 객체
	 * @return 		    판매자 메인 페이지로 이동
	 */
	@PostMapping("/product/modify")
	public String modifyProduct(int pno, Product product, MultipartFile imageFile) throws Exception {
		sellerService.modifyProduct(pno, product, imageFile);
		
		return "redirect:/seller/";
	}
	
	/** 별점 자릿수 계산 매서드
	 * @author 정동욱
	 * @param rating   평균별점을 받을 객체
	 * @param position 평균을 소수점 1자리까지만 출력하기 위해 자릿수를 정하는 객체
	 * @return 		   소수점 1자리까지 계산된 평균별점
	 */
	public static float getRating(float rating, int position) {
		float num = (float) Math.pow(10.0, position);
		
		return (Math.round(rating * num) / num);
	}

}












