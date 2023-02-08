package com.ezen.allit.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ezen.allit.domain.Hit;
import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.QnA;
import com.ezen.allit.repository.MemberRepository;
import com.ezen.allit.repository.QnARepository;
import com.ezen.allit.domain.Product;
import com.ezen.allit.dto.HitSaveRequestDto;
import com.ezen.allit.repository.HitRepository;
import com.ezen.allit.repository.MemberRepository;
import com.ezen.allit.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	private final MemberRepository memberRepo;
	private final ProductRepository productRepo;
	private final HitRepository hitRepo;
	private final QnARepository qnaRepo;


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
		
		memberRepo.save(member);
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


}
