package com.ezen.allit.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.ezen.allit.domain.Hit;
import com.ezen.allit.domain.Product;
import com.ezen.allit.domain.Review;
import com.ezen.allit.domain.Role;
import com.ezen.allit.domain.Seller;
import com.ezen.allit.dto.HitSaveRequestDto;
import com.ezen.allit.repository.HitRepository;
import com.ezen.allit.repository.ProductRepository;
import com.ezen.allit.repository.ReviewRepository;
import com.ezen.allit.repository.SellerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {
	private final SellerRepository sellerRepo;
	private final ProductRepository productRepo;
	private final HitRepository hitRepo;
	private final ReviewRepository reviewRepo;
//	private final BCryptPasswordEncoder encoder;
  
	/*
	 * 동욱파트
	 */
 
	// 판매자 로그인
	@Transactional
	public Seller findByIdAndPwd(String id, String pwd) {
		return sellerRepo.findByIdAndPwd(id, pwd);
	}

//	// 판매자 입점신청
//	@Transactional
//	public void saveSeller(Seller seller) {
//		String rawPwd = seller.getPwd();		// 입점신청 화면에서 넘겨받은 pwd
//		String encPwd = encoder.encode(rawPwd); // BCryptPasswordEncoder 클래스를 이용해 암호화
//		seller.setPwd(encPwd);
//		seller.setRole(Role.TEMP);
//		System.out.println("seller = " + seller);
//		sellerRepo.save(seller);
//	}
	
	// 판매자 입점신청
	@Transactional
	public void saveSeller(Seller seller) {
		seller.setRole(Role.SELLER);		
		sellerRepo.save(seller);
	}
	
	/*
	 * 판매자 상품목록조회
	 */
	@Transactional
	public Page<Product> getProductList(Pageable pageable, Seller seller) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 3;
		Page<Product> product = 
				productRepo.findAllBySellerId(seller.getId(), PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "pno")));

		/*
        System.out.println("product.getContent() = " + product.getContent()); 			  // 요청 페이지에 해당하는 글
        System.out.println("product.getTotalElements() = " + product.getTotalElements()); // 전체 글갯수
        System.out.println("product.getNumber() = " + product.getNumber()); 			  // DB로 요청한 페이지 번호
        System.out.println("product.getTotalPages() = " + product.getTotalPages()); 	  // 전체 페이지 갯수
        System.out.println("product.getSize() = " + product.getSize()); 				  // 한 페이지에 보여지는 글 갯수
        System.out.println("product.hasPrevious() = " + product.hasPrevious()); 		  // 이전 페이지 존재 여부
        System.out.println("product.isFirst() = " + product.isFirst()); 		  		  // 첫 페이지 여부
        System.out.println("product.isLast() = " + product.isLast()); 					  // 마지막 페이지 여부
		*/
		
        return product;
	}

	/*
	 * 판매자 상품조회
	 */
	@Transactional
	public Product getProduct(int pno) {		
		return productRepo.findById(pno).get();
	}

	/*
	 * 상품 조회수 증가
	 */
	@Transactional
	public void updateCount(int pno) {
		Product product = productRepo.findById(pno).get();
		product.setCount(product.getCount()+1);
	}
	
	/*
	 * 상품 좋아요
	 */
	@Transactional
	public void hitProduct(HitSaveRequestDto hitSaveRequestDto) {
		Optional<Hit> hit = hitRepo.findByProductPnoAndSellerId(hitSaveRequestDto.getPno(), hitSaveRequestDto.getSid());
		Product product = productRepo.findById(hitSaveRequestDto.getPno()).get();
		Seller seller = sellerRepo.findById(hitSaveRequestDto.getSid()).get();
		
		/* 이전에 좋아요 누른 기록이 없으면 좋아요, 있으면 좋아요 취소 */
		if(hit.isEmpty()) {
			hitRepo.save(new Hit(product, seller));
			product.setHit(product.getHit()+1);
		} else {
			hitRepo.deleteById(hit.get().getHno());
			product.setHit(product.getHit()-1);			
		}
	}
	
	/*
	 * 판매자 상품검색
	 */
	@Transactional
	public Page<Product> search(Seller seller, String searchKeyword, Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 3;
		
		Page<Product> product = 
				productRepo.findBySellerIdAndNameContaining(seller.getId(), searchKeyword, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "pno")));
		
		return product;
	}

	
	/*
	 * 판매자 상품등록
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
	 * 판매자 상품수정
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
	 * 판매자 상품삭제
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

	@Override
	public int idCheck(String id) {
		Optional<Seller> findId = sellerRepo.findById(id);
		
		if(findId.isPresent()) {
			System.out.println("이미 사용중인 아이디");
			return 0;
		} else {
			System.out.println("사용 가능한 아이디");
			return -1;
		}
	}

}
