package com.ezen.allit.domain;

import java.sql.Date;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.*;

@Getter
@Setter
@ToString
@Entity
public class QnA {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int qno;		 	   // 질문 일련번호
	private String category; 	   // 질문분류
	private String content;  	   // 내용
	private String status;   	   // 답변유무
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sid")
	private Seller seller;   	   // 판매자정보
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pno")
	private Product product; 	   // 상품정보
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "mid")
	private Member member;   // 질문자정보
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "rno")
	private Reply reply;         // 답변정보
	@CreationTimestamp
	private Date regDate;    	   // 등록일
}
