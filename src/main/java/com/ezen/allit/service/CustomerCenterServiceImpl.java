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
		int pageSize = 10;
		
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
	
	// 고객센터 글 등록
	@Override
	public void insertCustomerCenter(CustomerCenter cus, MultipartFile imageFile) throws Exception {
		
		CustomerCenter custo = new CustomerCenter();
		
		custo.setCategory(cus.getCategory());
		custo.setTitle(cus.getTitle());
		custo.setContent(cus.getContent());
		custo.setPick(cus.getPick());
		
		// 이미지 파일이 있으면
		if(imageFile != null) {

			String ogName = imageFile.getOriginalFilename(); 										  // 원본 파일명
			//String realPath = "c:/fileUpload/images/admin"; 	// 파일 저장경로
			String realPath = "c:/allit/images/admin/"; 	// 관리자용 이미지 저장경로
			
			/*
			 * UUID를 이용해 중복되지 않는 파일명 생성
			 */
			UUID uuid = UUID.randomUUID();
			String imgName = uuid + "_" + ogName; 		 // 저장될 파일명
			
			File saveDir = new File(realPath);
			if(!saveDir.isDirectory()) {
				// mkdir() : 해당 경로에 디렉토리가 존재하지 않으면 생성
				// mkdirs() :  mkdir()과 같으나 상위 폴더들이 없으면 상위 폴더들까지 생성
				if(saveDir.mkdirs()) {
					File saveFile = new File(realPath, imgName); // 저장경로와 파일명을 토대로 새 파일 생성 
					imageFile.transferTo(saveFile);			     // 생성 완료
					
					custo.setImageName(imgName);				 // DB에 저장될 파일명 (DB 저장을 위해 설정(없으면 DB에 저장 안됨))
				} else {
					System.out.println("[insertCustomerCenter()] "+ realPath + " : 디렉토리가 생성 중 오류");
				}
			} else {
				System.out.println("[insertCustomerCenter()] 폴더가 이미 있는 경우");
				
				File saveFile = new File(realPath, imgName); // 저장경로와 파일명을 토대로 새 파일 생성 
				imageFile.transferTo(saveFile);			     // 생성 완료
				
				custo.setImageName(imgName);
			}
		}
		cusRepo.save(custo);
			
	}

	// 관리자 고겍센터 게시글 수정
	@Override
	public void updateCusto(CustomerCenter cus, MultipartFile imageFile) throws Exception {
		// 수정하면서 이미지 파일을 추가 안할 때
		if(imageFile.getOriginalFilename().isEmpty()) {
			CustomerCenter custo = cusRepo.findById(cus.getCno()).get();
			
			// 수정 전 글에 이미지 파일이 있었다면 해당 이미지 파일 삭제
			if(custo.getImageName()!=null) {
				String realPath = "c:/allit/images/admin/"; 	// 상품 이미지파일 저장경로
				File oldFile = new File(realPath, custo.getImageName());
				oldFile.delete();
			}
			
			custo.setCategory(cus.getCategory());
			custo.setTitle(cus.getTitle());
			custo.setContent(cus.getContent());
			custo.setImageName(null);
			
			cusRepo.save(custo);
		// 수정하면서 이미지 파일도 수정
		}else if(imageFile != null) {
			
			CustomerCenter custo = cusRepo.findCustomerCenterByCno(cus.getCno());

			String ogName = imageFile.getOriginalFilename(); // 원본 파일명
			String realPath = "c:/allit/images/admin/"; 	// 상품 이미지파일 저장경로
			
			// 수정 전 글에 이미지 파일이 있었다면 해당 이미지 파일 삭제
			if(custo.getImageName()!=null) {
				File oldFile = new File(realPath, custo.getImageName());
				oldFile.delete();
			}
			
			/*
			 * UUID를 이용해 중복되지 않는 파일명 생성
			 */
			UUID uuid = UUID.randomUUID();
			String imgName = uuid + "_" + ogName; 		 // 저장될 파일명
			
			File saveDir = new File(realPath);
			if(!saveDir.isDirectory()) {
				// mkdir() : 해당 경로에 디렉토리가 존재하지 않으면 생성
				// mkdirs() :  mkdir()과 같으나 상위 폴더들이 없으면 상위 폴더들까지 생성
				if(saveDir.mkdirs()) {
					File saveFile = new File(realPath, imgName); // 저장경로와 파일명을 토대로 새 파일 생성 
					imageFile.transferTo(saveFile);			     // 생성 완료
					
					custo.setImageName(imgName);				 // DB에 저장될 파일명 (DB 저장을 위해 설정(없으면 DB에 저장 안됨))
				} else {
					System.out.println("[insertCustomerCenter()] "+ realPath + " : 디렉토리가 생성 중 오류");
				}
			} else {
				System.out.println("[insertCustomerCenter()] 폴더가 이미 있는 경우");
				
				File saveFile = new File(realPath, imgName); // 저장경로와 파일명을 토대로 새 파일 생성 
				imageFile.transferTo(saveFile);			     // 생성 완료
				
				custo.setImageName(imgName);
			}
			cusRepo.save(custo);	
		}
			
	}

	// 관리자 고객센터 게시글 삭제
	@Override
	public void deleteCusto(int cno) {

		cusRepo.deleteById(cno);		
	}

	// 고객센터 카테고리 별 조회
	@Override
	public Page<CustomerCenter> findCustomerCenterByCategoryContaining(String cate, Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 10;
		
		Page<CustomerCenter> custoList = 
				cusRepo.findCustomerCenterByCategoryContaining(cate, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "cno")));

        return custoList;

	}

	// 고객센터 메인 화면 등록 여부 조회
	@Override
	public List<CustomerCenter> findCustomerCenterByPick(String pick) {
		
		List<CustomerCenter> custoList = cusRepo.findCustomerCenterByPick(pick);
		return custoList;
	}

}
