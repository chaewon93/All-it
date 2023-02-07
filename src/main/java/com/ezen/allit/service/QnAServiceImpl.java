package com.ezen.allit.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ezen.allit.dto.QnaSaveRequestDto;
import com.ezen.allit.repository.QnARepository;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QnAServiceImpl implements QnAService {
	private final QnARepository qnaRepo;
	
	/*
	 * 상품문의 작성
	 */
	@Transactional
	public void saveQuestion(QnaSaveRequestDto qnaSaveRequestDto) {
		qnaRepo.saveQnA(qnaSaveRequestDto.getPno(), qnaSaveRequestDto.getSid(), qnaSaveRequestDto.getMid(), qnaSaveRequestDto.getCategory(), qnaSaveRequestDto.getContent());
	}
}
