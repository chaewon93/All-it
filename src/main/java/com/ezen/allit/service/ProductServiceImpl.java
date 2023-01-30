package com.ezen.allit.service;

import java.io.File;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ezen.allit.domain.Product;
import com.ezen.allit.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {
	@Autowired
	private ProductRepository productRepo;

	// 상품목록 조회
	@Transactional
	public Page<Product> getProductList(Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 12;
		
		Page<Product> product = 
				productRepo.findAll(PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "pno")));
		
        System.out.println("product.getContent() = " + product.getContent()); 			  // 요청 페이지에 해당하는 글
        System.out.println("product.getTotalElements() = " + product.getTotalElements()); // 전체 글갯수
        System.out.println("product.getNumber() = " + product.getNumber()); 			  // DB로 요청한 페이지 번호
        System.out.println("product.getTotalPages() = " + product.getTotalPages()); 	  // 전체 페이지 갯수
        System.out.println("product.getSize() = " + product.getSize()); 				  // 한 페이지에 보여지는 글 갯수
        System.out.println("product.hasPrevious() = " + product.hasPrevious()); 		  // 이전 페이지 존재 여부
        System.out.println("product.isFirst() = " + product.isFirst()); 		  		  // 첫 페이지 여부
        System.out.println("product.isLast() = " + product.isLast()); 					  // 마지막 페이지 여부
		
        return product;
	}
	
	// 상품 조회
	@Transactional
	public Product getProduct(Product product) {
		return productRepo.findById(product.getPno()).get();
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
	 * 상품검색
	 */
	@Transactional
	public Page<Product> search(String searchKeyword, Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 3;
		
		Page<Product> product = 
				productRepo.findByNameContaining(searchKeyword, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "pno")));
		
		return product;
	}
	
}














