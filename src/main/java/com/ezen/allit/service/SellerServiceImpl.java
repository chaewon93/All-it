package com.ezen.allit.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;
import java.util.UUID;

import com.ezen.allit.domain.OrdersDetail;
import com.ezen.allit.domain.Product;
import com.ezen.allit.domain.QnA;
import com.ezen.allit.domain.Role;
import com.ezen.allit.domain.Seller;
import com.ezen.allit.repository.OrdersDetailRepository;
import com.ezen.allit.repository.ProductRepository;
import com.ezen.allit.repository.QnARepository;
import com.ezen.allit.repository.SellerRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {
	private final SellerRepository sellerRepo;
	private final ProductRepository productRepo;
	private final OrdersDetailRepository ordersDetailRepo;
	private final QnARepository qnaRepo;
	private final BCryptPasswordEncoder encoder;
  
	/*
	 * 동욱파트
	 */
 
	// 판매자 로그인
	@Transactional
	public Seller findByIdAndPwd(String id, String pwd) {
		return sellerRepo.findByIdAndPwd(id, pwd);
	}
	
	/*
	 * 판매자 정보수정
	 */
	@Transactional
	public void modify(Seller seller) {
		Seller theSeller = sellerRepo.findById(seller.getId()).get();
		String rawPwd = seller.getPwd(); 	 	// 정보수정 시 작성한 password를 문자열에 담아
		String encPwd = encoder.encode(rawPwd); // PasswordEncoder객체를 이용해 해쉬화
		
		theSeller.setId(seller.getId());
		theSeller.setPwd(encPwd);
		theSeller.setName(seller.getName());
		theSeller.setContent(seller.getContent());
		theSeller.setEmail(seller.getEmail());
		theSeller.setPhone(seller.getPhone());
		theSeller.setZipcode(seller.getZipcode());
		theSeller.setAddress(seller.getAddress());
		theSeller.setRegno(seller.getRegno());
	}
	
	/*
	 * 판매자 탈퇴
	 */
	@Transactional
	public void quit(Seller seller) {
		sellerRepo.deleteById(seller.getId());
	}
	

	/*
	 *  판매자 입점신청
	 */
	@Transactional
	public void saveSeller(Seller seller) {
		String rawPwd = seller.getPwd();		// 입점신청 화면에서 넘겨받은 pwd
		String encPwd = encoder.encode(rawPwd); // BCryptPasswordEncoder 클래스를 이용해 암호화
		seller.setPwd(encPwd);
		seller.setRole(Role.TEMP);
		sellerRepo.save(seller);
	}

	/*
	 * 판매자 상품목록조회
	 */
	@Transactional
	public Page<Product> getProductList(Pageable pageable, Seller seller) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 3;
		Page<Product> productList = 
				productRepo.findAllBySellerIdAndStatus(seller.getId(), 1, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "pno")));

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
		
        return productList;
	}
	
	/*
	 * 판매자 상품검색
	 */
	@Transactional
	public Page<Product> search(Seller seller, String searchKeyword, Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 3;
		
		Page<Product> productList = 
				productRepo.findAllBySellerIdAndNameContainingAndStatus(seller.getId(), searchKeyword, 1, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "pno")));
		
		return productList;
	}
	
	/*
	 * 판매자 미등록상품목록 조회
	 */
	@Transactional
	public Page<Product> getUnregisteredProductList(Seller seller, Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 3;
		
		Page<Product> productList = 
				productRepo.findAllBySellerIdAndStatus(seller.getId(), 0, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "pno")));
		
		return productList;
	}

	/*
	 * 판매자 상품조회
	 */
	@Transactional
	public Product getProduct(int pno) {		
		return productRepo.findById(pno).get();
	}

	/*
	 * 판매자 주문목록조회 (검색조선 x, 검색어 x)
	 */
	@Transactional
	public Page<OrdersDetail> getOrderList(Seller seller, Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 10;
		
		Page <OrdersDetail> orderList = 
				ordersDetailRepo.findAllByProductSellerId(seller.getId(), PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "odno")));
		
		return orderList;
	}
	
	/*
	 * 판매자 주문목록조회 (검색조건 x, 검색어 o)
	 */
	@Transactional
	public Page<OrdersDetail> getSearhcedOrderList(Seller seller, String searchKeyword, Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 10;
		
		Page <OrdersDetail> orderList = 
				ordersDetailRepo.findAllByProductSellerIdAndProductNameContaining(seller.getId(), searchKeyword, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "odno")));
		
		return orderList;
	}
	
	
	/*
	 * 판매자 qna목록조회 (검색 x)
	 */
	@Transactional
	public Page<QnA> getQnAList(Seller seller, Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 10;
		
		Page <QnA> qnaList = 
				qnaRepo.findAllBySellerId(seller.getId(), PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "qno")));
		
		return qnaList;
	}
	
	/*
	 * 판매자 qna목록조회 (검색 o)
	 */
	@Transactional
	public Page<QnA> getSearchedQnAList(Seller seller, String searchKeyword, Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 10;
		
		Page <QnA> qnaList = 
				qnaRepo.findAllBySellerIdAndTitleContaining(seller.getId(), searchKeyword, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "qno")));
		
		return qnaList;
	}
	
	/*
	 * 판매자 qna조회
	 */
	@Transactional
	public QnA getQnA(int qno) {
		QnA qna = qnaRepo.findById(qno).get();
		
		return qna;
	}
	
	/*
	 * 판매자 상품등록
	 */
	@Transactional
	public void saveProduct(Product product, MultipartFile imageFile) throws Exception {
		String ogName = imageFile.getOriginalFilename(); 										  // 원본 파일명
		//String realPath = "c:/fileUpload/images/"; 	// 파일 저장경로
		
		String realPath = "c:/allit/images/product/"; 	// 상품 이미지파일 저장경로

		File saveDir = new File(realPath);
		if(!saveDir.isDirectory()) {
			
			// mkdir() : 해당 경로에 디렉토리가 존재하지 않으면 생성
			// mkdirs() :  mkdir()과 같으나 상위 폴더들이 없으면 상위 폴더들까지 생성
			if(saveDir.mkdirs()) {
				/*
				 * UUID를 이용해 중복되지 않는 파일명 생성
				 */
				UUID uuid = UUID.randomUUID();
				String imgName = uuid + "_" + ogName; 		 // 저장될 파일명
				
				File saveFile = new File(realPath, imgName); // 저장경로와 파일명을 토대로 새 파일 생성 
				imageFile.transferTo(saveFile);			     // 생성 완료
				
				product.setImageName(imgName);				 // DB에 저장될 파일명 (DB 저장을 위해 설정(없으면 DB에 저장 안됨))
				product.setMdPickyn(0);
		    product.setStatus(0);
    
				productRepo.save(product);
				
			} else {
				System.out.println("[saveProduct()] "+ realPath + " : 디렉토리가 생성 중 오류");
			}
			
		} else {
			System.out.println("[saveProduct()] "+ realPath + " : 디렉토리가 아니거나 존재하지 않음.");
		}
	}
	
	/*
	 * 판매자 상품수정
	 */
	@Transactional
	public void modifyProduct(int pno, Product product, MultipartFile imageFile) throws Exception {
		String ogName = imageFile.getOriginalFilename(); // 원본 파일명
		//String realPath = "c:/fileUpload/images/"; 		 // 파일 저장경로
		String realPath = "c:/allit/images/product/"; 	// 상품 이미지파일 저장경로
		
		/* 기존 이미지 파일 삭제 */
		File oldFile = new File(realPath, product.getImageName());
		oldFile.delete();
		
		File saveDir = new File(realPath);
		if(!saveDir.isDirectory()) {
			// mkdir() : 해당 경로에 디렉토리가 존재하지 않으면 생성
			// mkdirs() :  mkdir()과 같으나 상위 폴더들이 없으면 상위 폴더들까지 생성
			if(saveDir.mkdirs()) {
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
				
			} else {
				System.out.println("[modifyProduct()] "+ realPath + " : 디렉토리가 생성 중 오류");
			}
		} else {
			System.out.println("[modifyProduct()] "+ realPath + " : 디렉토리가 아니거나 존재하지 않음.");
		}
	}
	
	/*
	 * 판매자 상품삭제
	 */
	@Transactional
	public void deleteProduct(int pno) {
		productRepo.deleteById(pno);
	}

	/** 현일파트 **/
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
