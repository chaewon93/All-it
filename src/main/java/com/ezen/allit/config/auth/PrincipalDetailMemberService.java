package com.ezen.allit.config.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ezen.allit.domain.Member;
import com.ezen.allit.repository.MemberRepository;

import lombok.RequiredArgsConstructor;

/*
 * 시큐리티가 로그인을 처리할 때 (즉, http.loginProcessingUrl이 실행될 때)
 * UserDetailsService 타입으로 IoC 되어 있는 loadUserByUsername 매서드가 실행됨
 * 여기는 username만 처리하는 곳
 */
@Service
@RequiredArgsConstructor
public class PrincipalDetailMemberService implements UserDetailsService {
	private final MemberRepository memberRepo;
	
	/*
	 * UserDetails 타입으로 시큐리티 세션 저장소에 유저정보가 들어감
	 * 그리고 해당 매서드가 종료될 때 @AuthenticationPrincipal 어노테이션이 생성
	 */
	@Override
	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
		Member member = memberRepo.findById(id).get();
		
		if(member != null) {
			return new PrincipalDetailMember(member);
		} else {
			return null;
		}
	}

}
