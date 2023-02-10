package com.ezen.allit.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ezen.allit.domain.QnA;
import com.ezen.allit.dto.QnADto;
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
	public void saveQuestion(QnADto qnaDto) {
		qnaRepo.saveQnA(qnaDto.getPno(), qnaDto.getSid(), qnaDto.getMid(), qnaDto.getCategory(), qnaDto.getTitle(), qnaDto.getContent());
	}
	
	/*
	 * 상품문의 답변
	 */
	@Transactional
	public void saveResponse(QnADto qnaDto) {
		qnaRepo.responseQnA(qnaDto.getResponse(), qnaDto.getQno());
	}
	
	/*
	 * status 답변완료 변경
	 */
	@Transactional
	public void modifyStatus(QnADto qnaDto) {
		QnA qna = qnaRepo.findById(qnaDto.getQno()).get();
		qna.setStatus(1);
	}
}
