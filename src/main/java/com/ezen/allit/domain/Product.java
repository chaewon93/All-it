package com.ezen.allit.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Getter
@Setter
@ToString(exclude = {"seller", "review", "qna", "reply"})
@Entity
public class Product {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private int pno;			 	   			     // 상품 일련번호
	private int category; // 카테고리(1:패션, 2:식품, 3:주방용품, 4:생활용품, 5:인테리어, 6:가전, 7:스포츠/레저, 8:도서/음반/DVD, 9:반려동물용품, 10:건강식품)
	private String name;		 	   				 // 상품명
	private int price;			 	   				 // 최종판매가
	private int discount;			   				 // 할인
	private String content;		 	   				 // 내용
	private String imageName; 		   				 // 상품 이미지
	private float rating;		 	   				 // 상품별점
	private int count;				   				 // 조회수
	private int hit;				   				 // 좋아요
	private int status;  			  				 // 등록상태(0:등록신청, 1:등록완료)
	private int mdPickyn; 				  		     // MD픽(0:미등록, 1:등록)
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sid")
	private Seller seller;		 	   				 // 판매자정보
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties({"product"})
	@OrderBy(value = "rvno DESC")
	private List<Review> review = new ArrayList<>(); // 후기정보
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties({"product"})
	private List<QnA> qna = new ArrayList<>();		 // 문의정보
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties({"product"})
	private List<Reply> reply = new ArrayList<>();	 // 후기정보
	@OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties({"product"})
	private List<Hit> hits = new ArrayList<>();	  	 // 연관관계 설정용
	@CreationTimestamp
	private Date regDate; 		 	   				 // 등록일 
}
