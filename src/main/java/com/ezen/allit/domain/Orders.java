package com.ezen.allit.domain;

import java.util.*;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Getter
@Setter
@ToString
@Entity
public class Orders {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE) 
	private int ono;		 					 // 주문 일련번호
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "mid")
	private Member member;   					 // 사용자 정보
	@OneToMany(mappedBy = "orders", fetch = FetchType.EAGER)
	@JsonIgnoreProperties({"orders"}) // 무한참조 방지
	private List<OrdersDetail> ordersDetailList; // 연관관계 설정용
	@CreationTimestamp
	private Date regDate;	 					 // 구매일
}
