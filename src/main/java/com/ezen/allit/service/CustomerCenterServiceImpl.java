package com.ezen.allit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ezen.allit.domain.CustomerCenter;
import com.ezen.allit.domain.QnA;
import com.ezen.allit.repository.CustomerCenterRepository;

@Service
public class CustomerCenterServiceImpl implements CustomerCenterService {
	
	@Autowired
	CustomerCenterRepository cusRepo;

	// 관리자 고객센터 게시글 전체 조회(공지사항, 이벤트, 자주하는 질문, 신고 내역)
	@Override
	public Page<CustomerCenter> getCustomercenter(Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 6;
		
		Page<CustomerCenter> custoList = 
				cusRepo.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "cno")));

		return custoList;
	}

	// 관리자 고겍센터 게시글 작성
	@Override
	public void insertCustomerCenter(CustomerCenter cus) {

		CustomerCenter custo = new CustomerCenter();
		
		custo.setCategory(cus.getCategory());
		custo.setTitle(cus.getTitle());
		custo.setContent(cus.getContent());
		
		cusRepo.save(custo);		
	}

	// 관리자 고겍센터 게시글 수정
	@Override
	public void updateCusto(CustomerCenter cus) {
		
		CustomerCenter custo = cusRepo.findCustomerCenterByCno(cus.getCno());
		
		custo.setCategory(cus.getCategory());
		custo.setTitle(cus.getTitle());
		custo.setContent(cus.getContent());
		
		cusRepo.save(custo);		
	}

	// 관리자 고객센터 게시글 삭제
	@Override
	public void deleteCusto(int cno) {

		cusRepo.deleteById(cno);		
	}

	@Override
	public Page<CustomerCenter> findCustomerCenterByCategoryContaining(String cate, Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 6;
		
		Page<CustomerCenter> custoList = 
				cusRepo.findCustomerCenterByCategoryContaining(cate, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "cno")));

        return custoList;

	}

}
