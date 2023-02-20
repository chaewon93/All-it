package com.ezen.allit.service;

import java.io.File;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ezen.allit.domain.CustomerCenter;
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

	// 관리자 고객센터 게시글 작성
//	@Override
//	public void insertCustomerCenter(CustomerCenter cus) {
//
//		CustomerCenter custo = new CustomerCenter();
//		
//		custo.setCategory(cus.getCategory());
//		custo.setTitle(cus.getTitle());
//		custo.setContent(cus.getContent());
//		
//		cusRepo.save(custo);		
//	}
	
	@Override
	public void insertCustomerCenter(CustomerCenter cus, MultipartFile imageFile) throws Exception {

		CustomerCenter custo = new CustomerCenter();
		
		custo.setCategory(cus.getCategory());
		custo.setTitle(cus.getTitle());
		custo.setContent(cus.getContent());
		custo.setPick(cus.getPick());
		if(imageFile != null) {
			String ogName = imageFile.getOriginalFilename(); 										  // 원본 파일명
			String realPath = "c:/fileUpload/images/admin"; 	// 파일 저장경로
			
			/*
			 * UUID를 이용해 중복되지 않는 파일명 생성
			 */
			UUID uuid = UUID.randomUUID();
			String imgName = uuid + "_" + ogName; 		 // 저장될 파일명
			
			File saveFile = new File(realPath, imgName); // 저장경로와 파일명을 토대로 새 파일 생성 
			imageFile.transferTo(saveFile);			     // 생성 완료
			
			custo.setImageName(imgName);				 // DB에 저장될 파일명 (DB 저장을 위해 설정(없으면 DB에 저장 안됨))
		}
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

	@Override
	public List<CustomerCenter> findCustomerCenterByPick(String pick) {
		
		List<CustomerCenter> custoList = cusRepo.findCustomerCenterByPick(pick);
		return custoList;
	}

}
