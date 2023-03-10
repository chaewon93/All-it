package com.ezen.allit.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.ezen.allit.config.auth.PrincipalDetailMember;
import com.ezen.allit.domain.Cart;
import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.Orders;
import com.ezen.allit.domain.OrdersDetail;
import com.ezen.allit.domain.Product;
import com.ezen.allit.repository.CartRepository;
import com.ezen.allit.repository.MemberRepository;
import com.ezen.allit.repository.ProductRepository;
import com.ezen.allit.service.CouponService;
import com.ezen.allit.service.MemberService;
import com.ezen.allit.service.OrderService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
@SessionAttributes("user")
public class OrderController {
	private final ProductRepository productRepo;
	private final MemberRepository memberRepo;
	private final OrderService orderService;
	private final MemberService memberService;
	private final CartRepository cartRepo;
	private final CouponService couponService;
	
	@ModelAttribute("user")
	public Member setMember(@AuthenticationPrincipal PrincipalDetailMember principal) {
		if(principal != null) {
			Member member = principal.getMember();
			System.out.println("member=============== = " + member);
			return member;
		} else {
			return null;
		}
	}
	
	/** 즉시구매 시 주문/결제 페이지 요청 
	 * @author 정동욱
	 * @param product	주문할 상품 정보가 담긴 객체
	 * @param model		주문 페이지에 넘겨줄 정보 저장
	 * @param mid		주문하는 사용자 아이디
	 * @param quantity	상품 상세 페이지에서 넘겨준 수량
	 * @return			주문 페이지로 이동
	 */
	@PostMapping("/orderNow")
	public String getOrderView(Product product, Model model, String mid,
							@RequestParam("quantity") int quantity) {
		
		Member member = memberRepo.findById(mid).get();
		System.out.println("member = " + member);
		
		Product theProduct = productRepo.findById(product.getPno()).get();
		model.addAttribute("user", member);
		model.addAttribute("product", theProduct);
		model.addAttribute("quantity", quantity);
		
		return "mypage/orderInfo";
	}
	
	/** 장바구니에서 주문/결제 페이지 요청
	 * @author 임채원
	 * @param model		주문 페이지에 넘겨줄 정보 저장
	 * @param session	세션에 저장된 사용자 정보
	 * @param cno		장바구니 페이지에서 넘겨준 장바구니 번호
	 * @return			주문 페이지로 이동
	 */
	@PostMapping("/orderInfo")
	public String ordersView(Model model, HttpSession session,
							@RequestParam(value = "cno") int[] cno) {
		
		//System.out.println("[Orders ordersView()] cartList.size : "+cno.length);
		Member theMemeber = (Member) session.getAttribute("user");
		System.out.println("theMemeber = " + theMemeber);
		
		int totalPrice = 0;
		List<Cart> cartList = new ArrayList<>();
		
 		for(int i=0; i<cno.length; i++) {
			Cart cart = cartRepo.findById(cno[i]).get();
			cartList.add(cart);
			
			totalPrice += cart.getProduct().getPrice() * cart.getQuantity();
		}
 		
 		model.addAttribute("user", theMemeber);
 		model.addAttribute("cartList", cartList);
		model.addAttribute("totalPrice", totalPrice);
		//System.out.println("[Orders ordersView()] totalPrice : "+totalPrice);
		
		return "mypage/orderInfo";
	}

	/** 주문하기 - 바로구매
	 * @author 정동욱
	 * @param pno			구매할 상품 번호
	 * @param mid			구매자(사용자) 아이디
	 * @param model			세션에 수정된 사용자 정보를 저장
	 * @param ordersDetail	상품 상세정보가 담긴 객체
	 * @param finalPrice	총 결제 금액
	 * @param usePoint		사용한 포인트
	 * @param mcid			사용한 쿠폰번호
	 * @param couProid		
	 * @return				주문 내역 페이지로 이동
	 */
	@PostMapping("/order")
	public String insertOrder(int pno, String mid, Model model, OrdersDetail ordersDetail,
							@RequestParam(value = "finalPrice") int finalPrice,
							@RequestParam(value = "usePoint") int usePoint,
							@RequestParam(value = "memCou") int mcid,
							@RequestParam(value = "proid") int couProid) {   
		System.out.println("ordersDetail = " + ordersDetail);
		Product product = productRepo.findById(pno).get();
		Member member = memberRepo.findById(mid).get(); 
//		int amount = product.getPrice() * ordersDetail.getQuantity();

		// 1) Orders 테이블에 insert
		orderService.saveOrders(member, finalPrice, usePoint);
		// 2) OrdersDetail 테이블에 insert
		orderService.saveOrdersDetail(product, member, ordersDetail);

		// 3) 올잇머니 차감
		memberService.minusMoney(mid, finalPrice);
		
		// 4) 포인트 사용 시 포인트 차감
		if(usePoint != 0 && usePoint >= 1000) memberService.minusPoint(member.getId(), usePoint);

		// 5) 사용한 쿠폰 오더 디테일에 등록
		if(mcid != 0) {
			System.out.println("mcid = " + mcid);
			System.out.println("couProid = " + couProid);
			orderService.saveCouponOrder(mcid, couProid);
		}
				
		// 6) 세션에 수정된 정보 저장
		Member findMember = memberService.getMember(member);
		model.addAttribute("user", findMember);

		return "redirect:orderList";
	}
	
	/** 주문하기 - 장바구니 
	 * @author 임채원
	 * @param model			세션에 수정된 사용자 정보 저장
	 * @param ordersDetail	주문할 상품 정보가 담긴 객체
	 * @param member		구매자(사용자) 정보가 담긴 객체	
	 * @param cno			장바구니 페이지에서 선택한 장바구니 번호 배열
	 * @param finalPrice	총 결제 금액
	 * @param usePoint		사용한 포인트
	 * @param mcid			사용한 쿠폰 아이디
	 * @param couProid
	 * @return				주문 내역 페이지로 이동
	 */
	@PostMapping("/orders")
	public String insertOrders(Model model, OrdersDetail ordersDetail,
							@ModelAttribute("user") Member member,
							@RequestParam(value = "cno") int[] cno,
							@RequestParam(value = "finalPrice") int finalPrice,
							@RequestParam(value = "usePoint") int usePoint,
							@RequestParam(value = "memCou") int mcid,
							@RequestParam(value = "proid") int couProid) {
		
		// 1) Orders 테이블에 insert
		orderService.saveOrders(member, finalPrice, usePoint);
		
		for(int i=0; i<cno.length; i++) {
			Cart cart = cartRepo.findById(cno[i]).get();
			
			ordersDetail.setProduct(cart.getProduct());
			ordersDetail.setQuantity(cart.getQuantity());
			System.out.println("[order] ordersDetail : "+ordersDetail);
			
			// 2) OrdersDetail 테이블에 insert
			orderService.saveOrdersDetail(cart.getProduct(), member, ordersDetail);
			
			// 3) Cart 테이블에서 delete
			cartRepo.deleteById(cno[i]);
		}
		
		// 4) 올잇머니 차감
		//System.out.println("[order] price : "+price);
		memberService.minusMoney(member.getId(), finalPrice);
		
		// 5) 포인트 사용 시 포인트 차감
		if(usePoint != 0 && usePoint >= 1000) memberService.minusPoint(member.getId(), usePoint);
		
		// 6) 사용한 쿠폰 오더 디테일에 등록
		if(mcid != 0) {
			orderService.saveCouponOrder(mcid, couProid);
		}

		// 7) 세션에 수정된 정보 저장
		Member findMember = memberService.getMember(member);
		model.addAttribute("user", findMember);
		
		return "redirect:orderList";

	}
	
	/** 주문 목록조회
	 * @author 임채원
	 * @param model			주문 내역 페이지에 넘길 정보 저장
	 * @param principal		로그인시 저장된 사용자 정보
	 * @param pageable		주문 내역 페이지의 페이징을 처리에 필요한 객체
	 * @return				주문 내역 페이지로 이동
	 */
	@GetMapping("/orderList")
	public String getOrderList(Model model,
							@AuthenticationPrincipal PrincipalDetailMember principal,
							@PageableDefault(page = 1) Pageable pageable) {
		
		Page<Orders> orderList = orderService.getOrder(principal.getMember(), pageable);
		
		int naviSize = 10; // 페이지네이션 갯수
		int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / naviSize))) - 1) * naviSize + 1; // 1 11 21 31 ~~
	    int endPage = ((startPage + naviSize - 1) < orderList.getTotalPages()) ? startPage + naviSize - 1 : orderList.getTotalPages();
	    
	    model.addAttribute("list", orderList);
	    model.addAttribute("url", "/order/orderList/");
	    model.addAttribute("orderList", orderList);
	    model.addAttribute("startPage", startPage);
	    model.addAttribute("endPage", endPage);	    
	    
		
		return "mypage/orderList";
	}
	
	/** 주문 상세조회
	 * @author 임채원
	 * @param model		주문 상세 페이지에 넘겨줄 정보 저장
	 * @param ono		상세 조회할 주문 번호
	 * @param member	세션에 저장된 사용자 정보
	 * @return			주문 상세 페이지로 이동
	 */
	@PostMapping("/orderDetail/{ono}")
	public String orderDetail(Model model, @PathVariable int ono,
							@ModelAttribute("user") Member member) {
		System.out.println("ono = " + ono);
		int totalPrice = 0;
		int coupon = 0;
		
		//System.out.println("주문번호(ono) : "+ono);
		Orders order = new Orders();
		order.setOno(ono);
		
		List<OrdersDetail> orderDetailList = orderService.getOrderDetail(member, order);
		
		for(int i=0; i<orderDetailList.size(); i++) {
			totalPrice += orderDetailList.get(i).getProduct().getPrice() * orderDetailList.get(i).getQuantity();
			
			if(orderDetailList.get(i).getMemCoupon() != null) {
				int prodPrice = orderDetailList.get(i).getProduct().getPrice() * orderDetailList.get(i).getQuantity();
				if(orderDetailList.get(i).getMemCoupon().getCoupon().getDiscount() <= 100) {
					// 할인율 계산 -> 할인된 금액
					coupon = (prodPrice * orderDetailList.get(i).getMemCoupon().getCoupon().getDiscount()) / 100;
					
					// 할인 금액이 최대 할인금액보다 큰 경우
					if(coupon > orderDetailList.get(i).getMemCoupon().getCoupon().getMaxValue()) {
						coupon = orderDetailList.get(i).getMemCoupon().getCoupon().getMaxValue();
					}
				} else {
					// 할인된 금액
					coupon = orderDetailList.get(i).getMemCoupon().getCoupon().getDiscount();
				}
			}
		}
		//System.out.printf("총 상품금액 : %d, 쿠폰 : %d \n", totalPrice, coupon);
		model.addAttribute("totalPrice", totalPrice);
		model.addAttribute("coupon", coupon);
		model.addAttribute("orderDetailList", orderDetailList);
		
		return "mypage/orderDetail";
	}
	
	
	/** 주문 취소
	 * @author 임채원
	 * @param model		수정된 사용자 정보를 저장
	 * @param member	세션에 담긴 사용자 정보
	 * @param param		주문 내역 또는 주문 상세 페이지에서 넘긴 정보(주문 번호, 주문 상세 번호)
	 */
	@ResponseBody
	@PostMapping("/orderCancel")
	public void orderCancel(Model model, @ModelAttribute("user") Member member,
							@RequestBody Map<String, Object> param) {
		
		int cancelPrice = 0;
		int coupon = 0;
		int point = 0;
		
		// ono의 모든 주문 상세 정보 조회
		OrdersDetail orderDetail = orderService.getOrderDetail(Integer.parseInt(param.get("odno").toString()));
		
		// 총 상품금액(판매가 * 수량)
		int prodPrice = orderDetail.getProduct().getPrice() * orderDetail.getQuantity();

		// 쿠폰 사용 이력 체크
		if(orderDetail.getMemCoupon() != null) {
			if(orderDetail.getMemCoupon().getCoupon().getDiscount() <= 100) {
				// 할인율 계산 -> 할인된 금액
				coupon = (prodPrice * orderDetail.getMemCoupon().getCoupon().getDiscount()) / 100;

				// 할인 금액이 최대 할인금액보다 큰 경우
				if(coupon > orderDetail.getMemCoupon().getCoupon().getMaxValue()) {
					coupon = orderDetail.getMemCoupon().getCoupon().getMaxValue();
				}
			} else {
				// 할인된 금액
				coupon = orderDetail.getMemCoupon().getCoupon().getDiscount();
			}

			// MemCoupon의 status를 0(미사용)으로 변경
			couponService.updateStatus(orderDetail.getMemCoupon().getMcid(), 0);
		} 

		// 포인트 사용 이력 체크
		if(orderDetail.getOrders().getUsePoint() != 0) {
			point = orderDetail.getOrders().getUsePoint();
			
			// 포인트 복원
			//memberService.addPoint(member.getId(), point);
			member = memberRepo.findById(member.getId()).get();
			member.setPoint(member.getPoint() + point);
			
			// Orders의 usePoint 0으로 수정
			orderDetail.getOrders().setUsePoint(0);
		}

		// 취소금액 올잇머니 복원
		cancelPrice = prodPrice - coupon - point;
		memberService.addMoney(member.getId(), cancelPrice);

		// Orders의 finalPrice 수정 (포인트 적립때문에)
		orderService.updateOrders(orderDetail.getOrders().getOno(), orderDetail.getOrders().getFinalPrice() - cancelPrice);

		// OrdersDetail 삭제 -> status를 5(주문 취소)로 변경, 취소 신청일 저장
		orderService.refundOrder(5, null, orderDetail.getOdno());

		// 세션에 수정된 정보 저장
		Member findMember = memberService.getMember(member);
		model.addAttribute("user", findMember);
	}
	
	/** 구매확정
	 * @author 임채원
	 * @param model		수정된 사용자 정보 저장
	 * @param member	세션에 저장된 사용자 정보
	 * @param param		주문 내역 또는 주문 상세 페이지에서 ajax로 넘긴 데이터
	 */
	@ResponseBody
	@PostMapping("/orderComplete")
	public void orderComplete(Model model, @ModelAttribute("user") Member member,
							@RequestBody Map<String, Object> param) {
		//System.out.println("odno : "+param.get("odno").toString());
		
		// OrdersDetail의 status를 4(구매확정)로 변경
		orderService.updateStatus(4, Integer.parseInt(param.get("odno").toString()));
		
		// 포인트 적립(등급별 적립)
		memberService.addPoint(member.getId(), Integer.parseInt(param.get("finalPrice").toString()));
		
		// 세션에 수정된 정보 저장
		Member findMember = memberService.getMember(member);
		model.addAttribute("user", findMember);
		
	}
	
	/** 교환/반품 신청 
	 * @author 임채원
	 * @param member	세션에 저장된 사용자 정보
	 * @param param		주문 내역 또는 주문 상세 페이지에서 ajax로 넘겨준 데이터
	 */
	@ResponseBody
	@PostMapping("/orderRefund")
	public void orderRefund(@ModelAttribute("user") Member member,
						@RequestBody Map<String, Object> param) {
		
		System.out.println("[orderRefund()] param : "+param);
		int odno = Integer.parseInt(param.get("odno").toString());
		String reason = param.get("reason").toString();
		int status = 0;
		
		if(param.get("type").toString().equals("교환")) {
			status = 6;	// 교환 신청
		} else if(param.get("type").toString().equals("반품")) {
			status = 7;	// 반품 신청
		}
		
		orderService.refundOrder(status, reason, odno);
	}

}
