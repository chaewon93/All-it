package com.ezen.allit.domain;

import java.util.Date;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import lombok.*;

@Getter
@Setter
@ToString(exclude = {"seller", "product", "member", "qna"})
@Entity
public class Reply {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) 
	private int rno;		 // 답글 일련번호
	private String content;  // 내용
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pno")
	private Product product; // 상품정보
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sid")
	private Seller seller;   // 질문자정보
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "mid")
	private Member member;   // 질문자정보
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "qno")
	private QnA qna;   		 // 질문정보
	@CreationTimestamp
	private Date regDate;	 // 답변일
}
