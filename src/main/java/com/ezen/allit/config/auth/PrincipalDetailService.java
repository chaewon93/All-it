package com.ezen.allit.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.Role;
import com.ezen.allit.domain.Seller;
import com.ezen.allit.repository.MemberRepository;
import com.ezen.allit.repository.SellerRepository;

import lombok.RequiredArgsConstructor;

/*
 * 시큐리티가 로그인을 처리할 때 (즉, http.loginProcessingUrl이 실행될 때)
 * UserDetailsService 타입으로 IoC 되어 있는 loadUserByUsername 매서드가 실행됨
 * 여기는 username만 처리하는 곳
 */
@Service
@RequiredArgsConstructor
public class PrincipalDetailService implements UserDetailsService {
	private final MemberRepository memberRepo;
	private final SellerRepository sellerRepo;
	
	/*
	 * UserDetails 타입으로 시큐리티 세션 저장소에 로그인하는 객체의 정보가 들어감
	 * 그리고 해당 매서드가 종료될 때 @AuthenticationPrincipal 어노테이션이 생성
	 */
	@Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
		Member member = memberRepo.findById(id).orElse(null);
		Seller seller = sellerRepo.findById(id).orElse(null);
		System.out.println("member = " + member);
		System.out.println("seller = " + seller);

		if(member != null) {                      // 로그인시 member인지 확인
			return new PrincipalDetailMember(member);
		} else if(seller != null) {                // 로그인시 seller인지 확인
			if(seller.getRole() == Role.SELLER) {       // 역할이 판매자면
				return new PrincipalDetailSeller(seller);            
			} else if(seller.getRole() == Role.ADMIN) { // 역할이 관리자면
				return new PrincipalDetailSeller(seller);
			} else {                            		// 역할이 TEMP면
				return null;
			}
		} else {
			return null;
		}

	}

}
