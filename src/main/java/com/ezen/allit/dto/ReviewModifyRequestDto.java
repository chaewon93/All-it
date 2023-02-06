package com.ezen.allit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewModifyRequestDto {
	private int pno;
	private String sid;
	private int rvno;
	private String content;
	private int rating;
//	private int mid;
//	private String imageName;
}
