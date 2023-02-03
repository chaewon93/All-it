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
@ToString(exclude = {"qnaList", "orders", "ordersDetail", "review", "cart"})
@NoArgsConstructor
@Entity
public class Member {
	@Id
	private String id; 		   					 		   		 // 아이디
	private String pwd; 	   		 					   		 // 패스워드
	private String name; 	   		 					   		 // 이름
	private String email; 	   		 					   		 // 이메일
	private String phone;	   		 					   		 // 전화번호
	private String address;	   		 					   		 // 주소
	private String zipcode;	   		 					   		 // 우편번호
	private String birth;	   		 		 			  		 // 생년월일
	private String gender;	   		 		  			 		 // 성별
	private int money;		   		 		  			 		 // 충전액
	private int point;		   		 		   					 // 포인트
	private String provider;   		 		   					 // 로그인API 플랫폼
	private String providerId; 	 				 		   		 // 로그인API 플랫폼상 일련번호
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties({"member"})
	private List<Cart> cart = new ArrayList<>(); 				// 연관관계 설정
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties({"member"})
	private List<Orders> orders = new ArrayList<>();			 // 연관관계 설정
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties({"member"})
	private List<OrdersDetail> ordersDetail = new ArrayList<>(); // 연관관계 설정
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties({"member"})
	private List<Review> review = new ArrayList<>(); 			 // 연관관계 설정
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties({"member"})
	private List<QnA> qnaList = new ArrayList<>();				 // 연관관계 설정
	@OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties({"member"})
	private List<Hit> hits = new ArrayList<>();	  	   			 // 연관관계 설정용
	private Grade grade;	   		 		    			 	 // 회원등급
	@CreationTimestamp
	private Date regDate;			 	   		 		     	 // 가입일
	
	@Builder
	public Member(String id, String pwd, String name, String email, String phone, String address, String zipcode,
			String provider, String providerId, Grade grade, Date regDate) {
		super();
		this.id = id;
		this.pwd = pwd;
		this.name = name;
		this.email = email;
		this.phone = phone;
		this.address = address;
		this.zipcode = zipcode;
		this.provider = provider;
		this.providerId = providerId;
		this.grade = grade;
		this.regDate = regDate;
	}	
}
