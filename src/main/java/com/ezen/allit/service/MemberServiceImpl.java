package com.ezen.allit.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.ezen.allit.domain.Grade;
import com.ezen.allit.domain.Hit;
import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.OrdersDetail;
import com.ezen.allit.domain.QnA;
import com.ezen.allit.domain.Review;
import com.ezen.allit.domain.ReviewFile;
import com.ezen.allit.domain.Role;
import com.ezen.allit.domain.Seller;
import com.ezen.allit.repository.MemberRepository;
import com.ezen.allit.repository.OrdersDetailRepository;
import com.ezen.allit.repository.QnARepository;
import com.ezen.allit.repository.ReviewFileRepository;
import com.ezen.allit.repository.ReviewRepository;
import com.ezen.allit.repository.SellerRepository;
import com.ezen.allit.domain.Product;
import com.ezen.allit.dto.HitSaveRequestDto;
import com.ezen.allit.dto.ReviewDto;
import com.ezen.allit.repository.HitRepository;
import com.ezen.allit.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	private final MemberRepository memberRepo;
	private final ProductRepository productRepo;
	private final HitRepository hitRepo;
	private final QnARepository qnaRepo;
	private final ReviewRepository reviewRepo;
	private final ReviewFileRepository reviewFileRepo;
	private final SellerRepository sellerRepo;
	private final OrdersDetailRepository ordersDetailRepo;
	private final BCryptPasswordEncoder encoder;

	/** 회원 조회 */
	@Override
	public Member getMember(Member member) {
		
		Optional<Member> findMember = memberRepo.findById(member.getId());
		
		if(findMember.isPresent()) {
			return findMember.get();
		} else {
			return null;
		}
	}

	/** 회원 등록(회원가입) */
	@Override
	public void saveMember(Member member) {
		String rawPwd = member.getPwd();		// 회원가입 화면에서 넘겨받은 pwd
		String encPwd = encoder.encode(rawPwd); // BCryptPasswordEncoder 클래스를 이용해 암호화
		member.setPwd(encPwd);
		member.setRole(Role.MEMBER);

		memberRepo.save(member);
	}
	
	/** 회원 정보 수정 */
	@Override
	@Transactional
	public Member modifyMember(Member member) {
		Member theMember = memberRepo.findById(member.getId()).get();
		String rawPwd = member.getPwd();		// 회원가입 화면에서 넘겨받은 pwd
		String encPwd = encoder.encode(rawPwd); // BCryptPasswordEncoder 클래스를 이용해 암호화

		theMember.setPwd(encPwd);
		theMember.setEmail(member.getEmail());
		theMember.setPhone(member.getPhone());
		theMember.setZipcode(member.getZipcode());
		theMember.setAddress(member.getAddress());
		theMember.setBirth(member.getBirth());
		theMember.setGender(member.getGender());
		
		return theMember;
	}
	
	/** sns 회원 정보 수정 */
	@Override
	@Transactional
	public Member modifySnsMember(Member member) {
		System.out.println("member = " + member);
		Member theMember = memberRepo.findById(member.getId()).get();

		theMember.setEmail(member.getEmail());
		theMember.setPhone(member.getPhone());
		theMember.setZipcode(member.getZipcode());
		theMember.setAddress(member.getAddress());
		theMember.setBirth(member.getBirth());
		theMember.setGender(member.getGender());
		System.out.println("theMember = " + theMember);
		return theMember;
	}

	/** 아이디 중복확인 */
	@Override
	public int idCheck(String id) {
		Optional<Member> findId = memberRepo.findById(id);
		
		if(findId.isPresent()) {
			System.out.println("이미 사용중인 아이디");
			return 0;
		} else {
			System.out.println("사용 가능한 아이디");
			return -1;
		}
	}

	/** 아이디 찾기 */
	@Override
	public Member findById(Member member) {
		return memberRepo.findByNameAndEmail(member.getName(), member.getEmail());
	}

	/** 비밀번호 찾기 */
	@Override
	public Member findByPw(Member member) {
		return memberRepo.findByIdAndNameAndEmail(member.getId(), member.getName(), member.getEmail());
	}

	/** 회원 탈퇴 */
	@Override
	public void deleteMember(String id) {
		memberRepo.deleteById(id);
	}

	/** 1:1 문의하기(QnA) 글 등록 */
	@Override
	public void saveQna(QnA qna) {
		qnaRepo.save(qna);
	}

	/** 문의 내역 */
	@Override
	public Page<QnA> getQnaList(Member member, Pageable pageable) {
		
		int page = pageable.getPageNumber() - 1;
		int pageSize = 10;
		
		List<String> cate = new ArrayList<>();
		cate.add("상품문의");
		
		Page<QnA> qnaList = 
				qnaRepo.findByMemberAndCategoryNotIn(member, cate, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "qno")));
		
		return qnaList;
	}
 
 	/** 문의글 상세조회 */
	@Override
	public QnA getQnaDetail(int qno) {
		Optional<QnA> qna = qnaRepo.findById(qno);
		
		return qna.get();
	}

	/** 상품 좋아요 */
	@Transactional
	public void hitProduct(HitSaveRequestDto hitSaveRequestDto) {
		Optional<Hit> hit = hitRepo.findByProductPnoAndMemberId(hitSaveRequestDto.getPno(), hitSaveRequestDto.getMid());
		Product product = productRepo.findById(hitSaveRequestDto.getPno()).get();
		Member member = memberRepo.findById(hitSaveRequestDto.getMid()).get();
		
		/* 이전에 좋아요 누른 기록이 없으면 좋아요, 있으면 좋아요 취소 */
		if(hit.isEmpty()) {
			hitRepo.save(new Hit(product, member));
			product.setHit(product.getHit()+1);
		} else {
			hitRepo.deleteById(hit.get().getHno());
			product.setHit(product.getHit()-1);			
		}

	}
	
	/** 상품 구매 시 올잇머니 차감 */
	@Transactional
	@Override
	public void minusMoney(String id, int amount) {
		Member member = memberRepo.findById(id).get();
		member.setMoney(member.getMoney() - amount);
	}
	
	/** 취소/반품 시 올잇머니 환불 */
	@Transactional
	@Override
	public void addMoney(String id, int amount) {
		Member member = memberRepo.findById(id).get();
		member.setMoney(member.getMoney() + amount);
	}
	
	/** 상품 구매 시 포인트 사용 */
	@Transactional
	@Override
	public void minusPoint(String id, int amount) {
		Member member = memberRepo.findById(id).get();
		System.out.println("====================================minusPoint 포인트");
		System.out.println(amount);
		System.out.println(member);
		member.setPoint(member.getPoint() - amount);
		System.out.println(member);
	}

	/** 상품 구매 완료 후 포인트 적립 */
	@Transactional
	@Override
	public void addPoint(String id, int amount) {
		System.out.println("==========================================포인트 적립");
		Member member = memberRepo.findById(id).get();
		String grade = member.getGrade().toString();
		if(grade.equals(Grade.BRONZE.toString())) {
			member.setPoint(member.getPoint() + amount);
		}else if(grade.equals(Grade.SILVER.toString())) {
			 
		}else if(grade.equals(Grade.GOLD.toString())) {
			
		}else if(grade.equals(Grade.VIP.toString())) {
			
		}

	}
	
	/** 리뷰목록 조회 */
	@Transactional
	public Page<Review> getReviewList(String id, Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 10;
		
		Page<Review> reviewList = 
				reviewRepo.findAllByMemberId(id, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "rvno")));
		
		return reviewList;
	}

	/** 리뷰작성 */
	@Transactional
	public void saveReview(ReviewDto reviewDto) throws Exception {
		System.out.println("reviewDtoFile = " + reviewDto.getImageFile());
		Member member = memberRepo.findById(reviewDto.getMid()).get();
		Seller seller = sellerRepo.findById(reviewDto.getSid()).get();
		Product product = productRepo.findById(reviewDto.getPno()).get();
		OrdersDetail ordersDetail = ordersDetailRepo.findById(reviewDto.getOdno()).get();

		/** 파일 미첨부시 */
		if(reviewDto.getImageFile().get(0).isEmpty()) { // 여기 처리가 안돼요..ㅠㅠ
			System.out.println("aaaaaaaaaaaaaaaaaa");
			Review review = Review.toSaveReview(reviewDto);
			review.setMember(member);
			review.setSeller(seller);
			review.setProduct(product);
			review.setOrdersDetail(ordersDetail);
			ordersDetail.setStatus(8);
			
			reviewRepo.save(review);
		
		/** 파일 첨부시(다중가능) */
		} else {
			System.out.println("bbbbbbbbbbbbbbbbbbbbbbbb");
			Review review = Review.toSaveFileReview(reviewDto);
			review.setMember(member);
			review.setSeller(seller);
			review.setProduct(product);
			review.setOrdersDetail(ordersDetail);
			ordersDetail.setStatus(8);
			
			int theRvno = reviewRepo.save(review).getRvno();
			Review theReview = reviewRepo.findById(theRvno).get();
		
			for(MultipartFile imageFile : reviewDto.getImageFile()) {
				String ogName = imageFile.getOriginalFilename(); // 원본 파일명
				String realPath = "c:/fileUpload/images/"; 	// 파일 저장경로
				/*
				 * UUID를 이용해 중복되지 않는 파일명 생성
				 */
				UUID uuid = UUID.randomUUID();
				String imgName = uuid + "_" + ogName; 		 // 저장될 파일명
				
				File saveFile = new File(realPath, imgName); // 저장경로와 파일명을 토대로 새 파일 생성 
				imageFile.transferTo(saveFile);			     // 생성 완료
				System.out.println("saveFile = " + saveFile);
				ReviewFile reviewFile = ReviewFile.toSaveReviewFile(theReview, imgName);
				System.out.println("reviewFile = " + reviewFile);
				reviewFile.setRegDate(new Date());
				reviewFileRepo.save(reviewFile);
			}
		}
	}

}