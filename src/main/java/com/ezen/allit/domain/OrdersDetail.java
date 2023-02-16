package com.ezen.allit.domain;

import javax.persistence.*;

import lombok.*;

@Getter
@Setter
@ToString(exclude = {"orders", "member", "product"})
@Entity
public class OrdersDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private int odno;	    		// 주문상세 일련번호
	private int quantity;   		// 주문량
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "ono")
	private Orders orders;	 		// ono, quantity
	@ManyToOne
	@JoinColumn(name = "mid")
	private Member member;   		// mno
	@OneToOne
	@JoinColumn(name = "pno")
	private Product product; 		// pno
	@OneToOne
	@JoinColumn(name = "mcid")
	private MemCoupon memCoupon; 	// mcid
	private int status; // 주문상태(1:결제완료, 2:배송중, 3:배송완료, 4:구매확정, 5:주문취소, 6:교환신청, 7:반품신청)
	private String receiverName;	// 받는 사람 이름
	private String receiverZipcode;	// 받는 사람 우편번호
	private String receiverAddr;	// 받는 사람 주소
	private String receiverPhone;	// 받는 사람 전화번호
}
