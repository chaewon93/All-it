package com.ezen.allit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QnaSaveRequestDto {
	private int pno;
	private String sid;
	private String mid;
	private String category;
	private String content;
}
