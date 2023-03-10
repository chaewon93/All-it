package com.ezen.allit.service;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.ezen.allit.domain.Grade;
import com.ezen.allit.domain.Hit;
import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.OrdersDetail;
import com.ezen.allit.domain.Product;
import com.ezen.allit.domain.QnA;
import com.ezen.allit.domain.Review;
import com.ezen.allit.domain.ReviewFile;
import com.ezen.allit.domain.Role;
import com.ezen.allit.domain.Seller;
import com.ezen.allit.dto.HitDto;
import com.ezen.allit.dto.MemberDto;
import com.ezen.allit.dto.ReviewDto;
import com.ezen.allit.repository.HitRepository;
import com.ezen.allit.repository.MemberRepository;
import com.ezen.allit.repository.OrdersDetailRepository;
import com.ezen.allit.repository.ProductRepository;
import com.ezen.allit.repository.QnARepository;
import com.ezen.allit.repository.ReviewFileRepository;
import com.ezen.allit.repository.ReviewRepository;
import com.ezen.allit.repository.SellerRepository;

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
	
	// 
	@PersistenceContext
	private EntityManager entityManager;

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
		member.setGrade(Grade.BRONZE);
		member.setRegDate(new Date());

		memberRepo.save(member);
	}
	
	/** 회원 정보 수정 */
	@Override
	@Transactional
	public Member modifyMember(Member member) {
		Member theMember = memberRepo.findById(member.getId()).get();

		theMember.setEmail(member.getEmail());
		theMember.setPhone(member.getPhone());
		theMember.setZipcode(member.getZipcode());
		theMember.setAddress(member.getAddress());
		theMember.setBirth(member.getBirth());
		theMember.setGender(member.getGender());

		return theMember;
	}
	
	/** 구매화면에서 sns 회원 정보 수정 */
	@Override
	@Transactional
	public Member modifySnsMemberInfo(MemberDto memberDto) {
//		System.out.println("memberDto = " + memberDto);
		Member theMember = memberRepo.findById(memberDto.getId()).get();

		theMember.setEmail(memberDto.getEmail());
		theMember.setPhone(memberDto.getPhone());
		theMember.setZipcode(memberDto.getZipcode());
		theMember.setAddress(memberDto.getAddress1()+", "+memberDto.getAddress2());
		theMember.setBirth(memberDto.getBirth());
		theMember.setGender(memberDto.getGender());
//		System.out.println("theMember = " + theMember);
		
		return theMember;
	}
	
	/** 비밀번호변경을 위한 DB 비밀번호 확인(Dto) */
	@Override
	@Transactional
	public boolean checkPwd(MemberDto memberDto) {
		Member member = memberRepo.findById(memberDto.getId()).get();
		String dbPwd = member.getPwd();     // DB상 비밀번호
		String rawPwd = memberDto.getPwd(); // 입력한 비밀번호

		boolean match = encoder.matches(rawPwd, dbPwd); // encoder.matches(입력Pwd, DBPwd) 맞으면 true, 틀리면 false 반환

		return match;
	}
	
	/** 비밀번호변경을 위한 DB 비밀번호 확인(엔티티) */
	@Override
	@Transactional
	public boolean checkPwd2(Member member) {
		Member theMember = memberRepo.findById(member.getId()).get();
		String dbPwd = theMember.getPwd();     // DB상 비밀번호
		String rawPwd = member.getPwd(); // 입력한 비밀번호

		boolean match = encoder.matches(rawPwd, dbPwd); // encoder.matches(입력Pwd, DBPwd) 맞으면 true, 틀리면 false 반환

		return match;
	}
	
	/** 비밀번호 변경 팝업창에서 비밀번호 변경 */
	@Override
	@Transactional
	public Member modifyMemberPwd(MemberDto memberDto) {
		Member member = memberRepo.findById(memberDto.getId()).get();
		String rawPwd = memberDto.getPwd();		// 회원가입 화면에서 넘겨받은 pwd
		String encPwd = encoder.encode(rawPwd); // BCryptPasswordEncoder 클래스를 이용해 암호화

		member.setPwd(encPwd);

		return member;
	}
	
	/** 마이올잇에서 비밀번호변경 */
	@Override
	@Transactional
	public void modifyPwd(MemberDto memberDto) {
		Member member = memberRepo.findById(memberDto.getId()).get();
		String rawPwd = memberDto.getPwd();
		String encPwd = encoder.encode(rawPwd);
		
		member.setPwd(encPwd);
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
	public void hitProduct(HitDto hitDto) {
		Optional<Hit> hit = hitRepo.findByProductPnoAndMemberId(hitDto.getPno(), hitDto.getMid());
		Product product = productRepo.findById(hitDto.getPno()).get();
		Member member = memberRepo.findById(hitDto.getMid()).get();
		
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
	
	/** 올잇머니 충전, 취소/반품 시 올잇머니 환불 */
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
		member.setPoint(member.getPoint() - amount);
	}

	/** 상품 구매 완료 후 포인트 적립 */
	@Transactional
	@Override
	public void addPoint(String id, int finalPrice) {
		Member member = memberRepo.findById(id).get();
		String grade = member.getGrade().toString();
		if(grade.equals(Grade.BRONZE.toString())) {			// 1% 적립
			member.setPoint(member.getPoint() + finalPrice / 100);
		}else if(grade.equals(Grade.SILVER.toString())) {	// 3% 적립
			member.setPoint(member.getPoint() + finalPrice / 100 * 3);
		}else if(grade.equals(Grade.GOLD.toString())) {		// 5% 적립
			member.setPoint(member.getPoint() + finalPrice / 100 * 5);
		}else if(grade.equals(Grade.VIP.toString())) {		// 7% 적립
			member.setPoint(member.getPoint() + finalPrice / 100 * 7);
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
		Member member = memberRepo.findById(reviewDto.getMid()).get();
		Seller seller = sellerRepo.findById(reviewDto.getSid()).get();
		Product product = productRepo.findById(reviewDto.getPno()).get();
		OrdersDetail ordersDetail = ordersDetailRepo.findById(reviewDto.getOdno()).get();

		/** 파일 미첨부시 */
		if(reviewDto.getImageFile().get(0).isEmpty()) {
			Review review = Review.toSaveReview(reviewDto);
			review.setMember(member);
			review.setSeller(seller);
			review.setProduct(product);
			review.setOrdersDetail(ordersDetail);
			ordersDetail.setStatus(8);
			
			reviewRepo.save(review);
		
		/** 파일 첨부시(다중가능) */
		} else {
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
				//String realPath = "c:/fileUpload/images/"; 	// 파일 저장경로
				String realPath = "c:/allit/images/review/"; 	// 리뷰 이미지파일 저장경로
				
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
						
						ReviewFile reviewFile = ReviewFile.toSaveReviewFile(theReview, imgName); // reviewFile에 데이터 저장
						reviewFile.setRegDate(new Date());
						
						reviewFileRepo.save(reviewFile);
					} else {
						System.out.println("[saveReview()] "+ realPath + " : 디렉토리가 생성 중 오류");
					}
				} else {
					System.out.println("[saveReview()] 폴더가 이미 있는 경우");
					
					File saveFile = new File(realPath, imgName); // 저장경로와 파일명을 토대로 새 파일 생성 
					imageFile.transferTo(saveFile);			     // 생성 완료
					
					ReviewFile reviewFile = ReviewFile.toSaveReviewFile(theReview, imgName); // reviewFile에 데이터 저장
					reviewFile.setRegDate(new Date());
					
					reviewFileRepo.save(reviewFile);
				}
			}
		}
	}
	
	/** 좋아요목록 조회 */
	@Transactional
	public Page<Hit> getLikeList(String id, Pageable pageable) {
		int page = pageable.getPageNumber() - 1;
		int pageSize = 10;
		
		Page<Hit> likeList = 
				hitRepo.findAllByMemberIdAndProductNotNull(id, PageRequest.of(page, pageSize, Sort.by(Sort.Direction.DESC, "hno")));
		
		System.out.println("likeList = " + likeList);
		return likeList;
	}
	
	/** 리뷰삭제 */
	@Transactional
	public void deleteHit(HitDto hitDto) {
		Hit hit = hitRepo.findById(hitDto.getHno()).get();
		hitRepo.deleteById(hit.getHno());
	}

}
