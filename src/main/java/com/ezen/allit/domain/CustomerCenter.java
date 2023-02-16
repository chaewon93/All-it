package com.ezen.allit.domain;

import javax.persistence.*;

import lombok.*;

@Getter
@Setter
@ToString
@Entity
public class CustomerCenter {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int cno;		 // 질문 일련번호
	private String category; // 질문분류
	private String title;    // 제목
	private String content;  // 내용
	private String imageName;	// 상품 이미지
	private String pick;		// 메인화면에 나올 글 선택
}
