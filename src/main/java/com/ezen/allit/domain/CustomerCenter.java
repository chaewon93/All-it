package com.ezen.allit.domain;

import javax.persistence.*;

import lombok.*;

@Getter
@Setter
@ToString
@Entity
public class CustomerCenter {
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private int cno;		 // 질문 일련번호
	private String category; // 질문분류
	private String title;    // 제목
	private String content;  // 내용
}
