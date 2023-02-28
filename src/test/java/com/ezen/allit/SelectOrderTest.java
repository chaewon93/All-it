package com.ezen.allit;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.Orders;
import com.ezen.allit.domain.OrdersDetail;
import com.ezen.allit.repository.OrdersDetailRepository;
import com.ezen.allit.repository.OrdersRepository;

@SpringBootTest
public class SelectOrderTest {

	@Autowired
	private OrdersRepository orderRepo;
	
	@Test
//	@Disabled
	public void selectOrder() {
		Optional<Orders> results = orderRepo.findById(3);
		
		if (results.isPresent()) {
			Orders order = results.get();
			
			System.out.println("=====>  주문번호: " + order.getOno());
			System.out.println("\t회원명: " + order.getMember().getId());
			
			System.out.println("--------------------------------");
			for(OrdersDetail od : order.getOrdersDetail()) {
				System.out.println(od);
			}
			System.out.println("--------------------------------");
		} else {
			System.out.println("상세 정보 없음");
		}
	}
	
//	@Test
//	public void getOrderList() {
//		Member member = new Member();
//		member.setId("aaaa2");
//		
//		<Orders> orders = orderRepo.findAllByMemberId(member.getId());
//		
//		for(int i=0; i<orders.size(); i++) {	// 'aaaa2' 사용자에 대한 orders : 2개
//			// orders 2개에 대한 orderDetails 각각 1개, 2개
//			System.out.println("[Order] getOrderList() - orderdetail size : "+orders.get(i).getOrdersDetail().size());
//			
//			for(int j=0; j<orders.get(i).getOrdersDetail().size(); j++) {
//				System.out.println("====> 상품번호 : "+orders.get(i).getOrdersDetail().get(j).getProduct().getPno());
//				System.out.println("====> 수량 : "+orders.get(i).getOrdersDetail().get(j).getQuantity());
//				System.out.println("====> 가격 : "+orders.get(i).getOrdersDetail().get(j).getProduct().getPrice());
//				System.out.println("====> 주문상태 : "+orders.get(i).getOrdersDetail().get(j).getStatus());
//			}
//		}
//	}
}
