package com.ezen.allit.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ezen.allit.domain.Hit;
import com.ezen.allit.domain.Member;
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

	@Override
	public Member getMember(Member member) {
		
		Optional<Member> findMember = memberRepo.findById(member.getId());
		
		if(findMember.isPresent()) {
			return findMember.get();
		} else {
			return null;
		}
	}

	@Override
	public void saveMember(Member member) {
		
		memberRepo.save(member);
		
	}

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

	@Override
	public Member findById(Member member) {
		return memberRepo.findByNameAndEmail(member.getName(), member.getEmail());
	}

	@Override
	public Member findByPw(Member member) {
		return memberRepo.findByIdAndNameAndEmail(member.getId(), member.getName(), member.getEmail());
	}

	@Override
	public void deleteMember(String id) {
		memberRepo.deleteById(id);
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
