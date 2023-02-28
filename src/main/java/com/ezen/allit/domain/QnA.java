package com.ezen.allit.domain;

import java.sql.Date;

import javax.persistence.*;

import org.hibernate.annotations.CreationTimestamp;

import lombok.*;

@Getter
@Setter
@ToString(exclude = {"seller", "product", "member", "reply"})
@Entity
public class QnA {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int qno;		 // 질문 일련번호
	private String title;	 // 제목
	private String category; // 질문분류
	private String content;  // 내용
	private String response; // 답변
	private int status;      // 답변유무(0:미답변, 1:답변)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sid")
	private Seller seller;   // 판매자정보
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pno")
	private Product product; // 상품정보
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mid")
	private Member member;   // 질문자정보
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "rno")
	private Reply reply;     // 답변정보
	@CreationTimestamp
	private Date regDate;    // 등록일
	@CreationTimestamp
	private Date resDate;    // 답변일
}
