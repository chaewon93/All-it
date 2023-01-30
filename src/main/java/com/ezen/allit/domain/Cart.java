package com.ezen.allit.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Getter
@Setter
@ToString(exclude = {"member", "productList"})
@Entity
public class Cart {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private int cno;		  	 		 // 장바구니 일련번호
	private int quantity;	   			 // 수량
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "mid")
	private Member member;	   			 // mno, mname
	@OneToMany(mappedBy = "cart", fetch = FetchType.EAGER)
	@JsonIgnoreProperties({"cart"}) // 무한참조 방지
	private List<Product> productList = new ArrayList<>();   // pno, pname, price2
}
