package com.ezen.allit.dto;

import lombok.*;

@Getter
@Setter
@ToString

public class MemberDto {
	private String id; 		   					 		   		 // 아이디
	private String pwd; 	   		 					   		 // 패스워드
	private String name; 	   		 					   		 // 이름
	private String email; 	   		 					   		 // 이메일
	private String phone;	   		 					   		 // 전화번호
	private String address1;		   		 					   	 // 주소
	private String address2;		   		 					   	 // 주소
	private String zipcode;	   		 					   		 // 우편번호
	private String birth;	   		 		 			  		 // 생년월일
	private String gender;	   		 		  			 		 // 성별
	private int money;		   		 		  			 		 // 충전액
	private int point;		   		 		   					 // 포인트
	private String provider;   		 		   					 // 로그인API 플랫폼
	private String providerId; 	 				 		   		 // 로그인API 플랫폼상 일련번호

}
