package com.ezen.allit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewSaveRequestDto {
	private String content;
	private String imageName;
	private int rating;
	private String mid;
	private int pno;
}
