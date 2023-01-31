package com.ezen.allit.domain;

import java.util.*;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Getter
@Setter
@ToString(exclude = {"orders", "member", "product"})
@Entity
public class OrdersDetail {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private int odno;	    						   // 주문상세 일련번호
	private int quantity;   		 				   // 주문량
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "ono")
	private Orders orders;	 						   // ono, quantity
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "mid")
	private Member member;   						   // mno
	@OneToMany(mappedBy = "ordersDetail", fetch = FetchType.LAZY)
	@JsonIgnoreProperties({"ordersDetail"})
	private List<Product> product = new ArrayList<>(); // pno
	private int status; // 주문상태(1:결제완료, 2:배송중, 3:배송완료, 4:구매확정, 5:주문취소)
}
