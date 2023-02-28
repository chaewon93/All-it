package com.ezen.allit.domain;

import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Getter
@Setter
@ToString(exclude = {"member", "ordersDetail"})
@Entity
public class Orders {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private int ono;											// 주문 일련번호
	private int finalPrice;										// 최종결제금액
	private int usePoint;										// 사용한 포인트
	@ManyToOne
	@JoinColumn(name = "mid")
	private Member member;										// 사용자 정보
	@OneToMany(mappedBy = "orders", fetch = FetchType.LAZY)
	@JsonIgnoreProperties({"orders"})
	private List<OrdersDetail> ordersDetail = new ArrayList<>(); // 연관관계 설정용
	@CreationTimestamp
	private Date regDate;	 									 // 구매일
}
