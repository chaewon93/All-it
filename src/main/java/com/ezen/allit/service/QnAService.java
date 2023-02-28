package com.ezen.allit.service;

import com.ezen.allit.dto.QnADto;

public interface QnAService {
	void saveQuestion(QnADto qnaDto);
	
	void saveResponse(QnADto qnaDto);
	
	void modifyStatus(QnADto qnaDto);
	
	void deleteResponse(QnADto qnaDto);
	
	void undoStatus(QnADto qnaDto);
}
