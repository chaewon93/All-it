package com.ezen.allit.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.ezen.allit.domain.Cart;
import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.Orders;
import com.ezen.allit.domain.OrdersDetail;
import com.ezen.allit.domain.Product;
import com.ezen.allit.repository.CartRepository;
import com.ezen.allit.repository.MemberRepository;
import com.ezen.allit.repository.ProductRepository;
import com.ezen.allit.service.MemberService;
import com.ezen.allit.service.OrderService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/order")
@SessionAttributes("user")
@RequiredArgsConstructor
public class OrderController {
	private final ProductRepository productRepo;
	private final MemberRepository memberRepo;
	private final OrderService orderService;
	private final MemberService memberService;
	private final CartRepository cartRepo;
	
	@ModelAttribute("user")
	public Member setMember() {
		return new Member();
	}
	
	/** 즉시구매 시 주문/결제 페이지 요청 */
	@PostMapping("/orderNow")
	public String getOrderView(Product product, Model model,
							@RequestParam("quantity") int quantity) {
		
		Product theProduct = productRepo.findById(product.getPno()).get();
		model.addAttribute("product", theProduct);
		model.addAttribute("quantity", quantity);
		
		return "mypage/orderInfo";
	}
	
	/** 장바구니에서 주문/결제 페이지 요청 */
	@PostMapping("/orderInfo")
	public String ordersView(Model model, @ModelAttribute("user") Member member,
							@RequestParam(value = "cno") int[] cno) {
		
		//System.out.println("[Orders ordersView()] cartList.size : "+cno.length);
		
		int totalPrice = 0;
		List<Cart> cartList = new ArrayList<>();
		
 		for(int i=0; i<cno.length; i++) {
			Cart cart = cartRepo.findById(cno[i]).get();
			cartList.add(cart);
			
			totalPrice += cart.getProduct().getPrice() * cart.getQuantity();
		}
 		
 		model.addAttribute("cartList", cartList);
		model.addAttribute("totalPrice", totalPrice);
		//System.out.println("[Orders ordersView()] totalPrice : "+totalPrice);
		
		return "mypage/orderInfo";
	}

	/** 주문하기 - 바로구매 */
	@PostMapping("/order")
	public String insertOrder(int pno, String mid, Model model, OrdersDetail ordersDetail,
							@RequestParam(value = "price") int price,
							@RequestParam(value = "point") int point,
							@RequestParam(value = "memCou") int mcid,
							@RequestParam(value = "proid") int couProid) {   
		System.out.println("ordersDetail = " + ordersDetail);

		Product product = productRepo.findById(pno).get();
		Member member = memberRepo.findById(mid).get(); 
//		int amount = product.getPrice() * ordersDetail.getQuantity();

		// 1) Orders 테이블에 insert
		orderService.saveOrders(member);
		// 2) OrdersDetail 테이블에 insert
		orderService.saveOrdersDetail(product, member, ordersDetail);

		// 3) 올잇머니 차감
		memberService.minusMoney(mid, price);
		
		// 4) 포인트 사용 시 포인트 차감
		if(point != 0 && point >= 1000) memberService.minusPoint(member.getId(), point);

		// 포인트 적립 : 결제금액의 1% -> 구매확정 후 적립
		if(point == 0) {
			memberService.addPoint(member.getId(), price / 100);
		}
		
		// 사용한 쿠폰 오더 디테일에 등록
		if(mcid != 0) {
			orderService.saveCouponOrder(mcid, couProid);
		}
				
		// 5) 세션에 수정된 정보 저장
		Member findMember = memberService.getMember(member);
		model.addAttribute("user", findMember);

		return "redirect:orderList";
	}
	
	/** 주문하기 - 장바구니 */
	@PostMapping("/orders")
	public String insertOrders(Model model, OrdersDetail ordersDetail,
							@ModelAttribute("user") Member member,
							@RequestParam(value = "cno") int[] cno,
							@RequestParam(value = "price") int price,
							@RequestParam(value = "point") int point,
							@RequestParam(value = "memCou") int mcid,
							@RequestParam(value = "proid") int couProid) {	
		
		// 1) Orders 테이블에 insert
		orderService.saveOrders(member);
		
		for(int i=0; i<cno.length; i++) {
			Cart cart = cartRepo.findById(cno[i]).get();
			
			ordersDetail.setProduct(cart.getProduct());
			ordersDetail.setQuantity(cart.getQuantity());
			//System.out.println("[order] ordersDetail : "+ordersDetail);
			
			// 2) OrdersDetail 테이블에 insert
			orderService.saveOrdersDetail(cart.getProduct(), member, ordersDetail);
			
			// 3) Cart 테이블에서 delete
			cartRepo.deleteById(cno[i]);
		}
		
		// 4) 올잇머니 차감
		//System.out.println("[order] price : "+price);
		memberService.minusMoney(member.getId(), price);
		
		// 5) 포인트 사용 시 포인트 차감
		if(point != 0 && point >= 1000) memberService.minusPoint(member.getId(), point);
		
		// 포인트 적립 : 결제금액의 1% -> 구매확정 후 적립
		if(point == 0) {
			memberService.addPoint(member.getId(), price / 100);
		}
		
		// 사용한 쿠폰 오더 디테일에 등록
		if(mcid != 0) {
			orderService.saveCouponOrder(mcid, couProid);
		}

		// 6) 세션에 수정된 정보 저장
		Member findMember = memberService.getMember(member);
		model.addAttribute("user", findMember);
		
		return "redirect:orderList";

	}
	
	/** 주문 목록조회 */
	@GetMapping("/orderList")
	public String getOrderList(Model model,
							@ModelAttribute("user") Member member,
							@PageableDefault(page = 1) Pageable pageable) {
		
		Page<Orders> orderList = orderService.getOrder(member, pageable);
		
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
	
	/** 주문 상세조회 */
	@PostMapping("/orderDetail")
	public String orderDetail(Model model, Orders order,
							@ModelAttribute("user") Member member) {
		
		// 보여줄 정보
		// 1) Orders - 주문일자
		// 2) OrdersDetail - 상품정보(pno), 받는사람 정보(이름, 연락처, 주소), 결제정보(
		
		int totalPrice = 0;
		int coupon = 0;
		List<OrdersDetail> orderDetailList = orderService.getOrderDetail(member, order);
		
		for(int i=0; i<orderDetailList.size(); i++) {
			totalPrice += orderDetailList.get(i).getProduct().getPrice() * orderDetailList.get(i).getQuantity();
			
			if(orderDetailList.get(i).getMemCoupon() != null) {
				int prodPirce = orderDetailList.get(i).getProduct().getPrice() * orderDetailList.get(i).getQuantity();
				if(orderDetailList.get(i).getMemCoupon().getCoupon().getDiscount() <= 100) {
					// 할인율 계산
					coupon = (prodPirce * orderDetailList.get(i).getMemCoupon().getCoupon().getDiscount()) / 100;
				} else {
					// 금액 할인
					coupon = orderDetailList.get(i).getMemCoupon().getCoupon().getDiscount();
				}
			}
		}
		System.out.printf("총 상품금액 : %d, 쿠폰 : %d", totalPrice, coupon);
		model.addAttribute("totalPrice", totalPrice);
		model.addAttribute("coupon", coupon);
		model.addAttribute("orderDetailList", orderDetailList);
		
		return "mypage/orderDetail";
	}
	
	
	/** 주문 취소 */
	@PostMapping("/orderCancel")
	public String orderCancel() {
		
		// OrdersDetail에서 주문 상세 정보 삭제
		
		// Orders에서 주문 삭제
		
		return "";
	}

}
