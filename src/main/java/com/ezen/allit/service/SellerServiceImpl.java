package com.ezen.allit.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

import com.ezen.allit.domain.Product;
import com.ezen.allit.domain.Seller;
import com.ezen.allit.repository.ProductRepository;
import com.ezen.allit.repository.SellerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {
	private final SellerRepository sellerRepo;
	private final ProductRepository productRepo;
  
	/*
	 * 동욱파트
	 */
 
	// 판매자 로그인
	@Transactional
	public Seller findByIdAndPwd(String id, String pwd) {
		return sellerRepo.findByIdAndPwd(id, pwd);
	}

	// 판매자 입점신청
	@Transactional
	public void saveSeller(Seller seller) {
		sellerRepo.save(seller);
	}
	
	/*
	 *  상품등록
	 */
	@Transactional
	public void saveProduct(Product product, MultipartFile imageFile) throws Exception {
		String ogName = imageFile.getOriginalFilename(); 										  // 원본 파일명
		String realPath = "c:/fileUpload/images/"; 	// 파일 저장경로
		
		/*
		 * UUID를 이용해 중복되지 않는 파일명 생성
		 */
		UUID uuid = UUID.randomUUID();
		String imgName = uuid + "_" + ogName; 		 // 저장될 파일명
		
		File saveFile = new File(realPath, imgName); // 저장경로와 파일명을 토대로 새 파일 생성 
		imageFile.transferTo(saveFile);			     // 생성 완료
		
		product.setImageName(imgName);				 // DB에 저장될 파일명 (DB 저장을 위해 설정(없으면 DB에 저장 안됨))
		
		productRepo.save(product);
	}
	
	/*
	 * 상품수정
	 */
	@Transactional
	public void modifyProduct(int pno, Product product, MultipartFile imageFile) throws Exception {
		String ogName = imageFile.getOriginalFilename(); // 원본 파일명
		String realPath = "c:/fileUpload/images/"; 		 // 파일 저장경로
		
		/*
		 * 기존 이미지 파일 삭제
		 */
		File oldFile = new File(realPath, product.getImageName());
		oldFile.delete();
		
		/*
		 * UUID를 이용해 중복되지 않는 파일명 생성
		 */
		UUID uuid = UUID.randomUUID();
		String imgName = uuid + "_" + ogName; 			 // 저장될 파일명
		
		File saveFile = new File(realPath, imgName);     // 저장경로와 파일명을 토대로 새 파일 생성 
		imageFile.transferTo(saveFile);					 // 생성 완료
		
		Product theProduct = productRepo.findById(pno).get();
		theProduct.setCategory(product.getCategory());
		theProduct.setName(product.getName());
		theProduct.setPrice(product.getPrice());
		theProduct.setContent(product.getContent());
		theProduct.setImageName(imgName);
	}
	
	
	/*
	 * 상품삭제
	 */
	@Transactional
	public void deleteProduct(int pno) {
		productRepo.deleteById(pno);
	}


	/*
	 * 현일파트
	 */
	@Override
	public Seller getSeller(Seller seller) {		
		
		System.out.println("-----------------------------findSeller_Impl");
		System.out.println(seller);
		System.out.println("-----------------------------");
		
		Optional<Seller> findSeller = sellerRepo.findById(seller.getId());
		
		System.out.println("-----------------------------findSeller_findById");
		System.out.println(findSeller);
		System.out.println("-----------------------------");
		
		if(findSeller.isPresent()) {
			System.out.println("-----------------------------findSeller_Impl");
			System.out.println(findSeller);
			System.out.println("-----------------------------");
			return findSeller.get();
		}
		else return null;
	}

}
