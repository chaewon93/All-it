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
@ToString(exclude = {"product", "review", "qna", "reply"})
@Entity
public class Seller {
	@Id
	private String id; 		   		   				   // 아이디
	private String pwd; 	   		   				   // 패스워드
	private String name; 	   		   				   // 이름
	private String email; 	   		   				   // 이메일
	private String phone;	   		   				   // 전화번호
	private String address;	   		   				   // 주소
	private String zipcode;	   		   				   // 우편번호
	private String regno; 	   		   				   // 사업자번호 
	private String content;    		   				   // 판매자 설명
	@OneToMany(mappedBy = "seller", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties({"seller"})
	private List<Product> product = new ArrayList<>(); // 연관관계 설정용
	@OneToMany(mappedBy = "seller", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties({"seller"})
	private List<Review> review = new ArrayList<>();   // 연관관계 설정용
	@OneToMany(mappedBy = "seller", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties({"seller"})
	private List<QnA> qna = new ArrayList<>();		   // 연관관계 설정용
	@OneToMany(mappedBy = "seller", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties({"seller"})
	private List<Reply> reply = new ArrayList<>();	   // 연관관계 설정용
	private Role role;		   		   				   // 역할구분(0:temp, 1:seller, 2:admin)
	@CreationTimestamp
	private Date regDate; 	   		   				   // 가입일
}
