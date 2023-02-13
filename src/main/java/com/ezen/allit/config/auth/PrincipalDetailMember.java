package com.ezen.allit.config.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.ezen.allit.domain.Member;

import lombok.Data;

/*
 * SecurityConfig의 http.loginProcessingUrl가 로그인 요청을 낚아채 시큐리티 로그인을 진행
 * 로그인이 완료되면 시큐리티 세션을 생성 (Security ContextHolder)
 * 시큐리티 세션에는 Authentication 타입의 객체만 들어갈 수 있음
 * Authentication 객체의 유저정보는 UserDetail에 저장함
 * 즉, 로그인 완료 시 UserDetails 타입의 오브젝트를 시큐리티의 고유 세션 저장소에 저장
 * 그리고, OAuth는 OAuth2User 타입의 오브젝트를 시큐리티의 고유 세션 저장소에 저장
 */
@Data
public class PrincipalDetailMember implements UserDetails, OAuth2User {
	private static final long serialVersionUID = 2L;
	private Member member; // 포함관계로 포함
	private Map<String, Object> attributes;

	// 시큐리티 로그인 시 사용
	public PrincipalDetailMember(Member member) {
		this.member = member;
	}
	
	// OAuth 로그인 시 사용
	public PrincipalDetailMember(Member member, Map<String, Object> attributes) {
		this.member = member;
		this.attributes = attributes;
	}

	@Override
	public String getPassword() {
		return member.getPwd();
	}

	@Override
	public String getUsername() {
		return member.getId();
	}
	
	@Override
	public String getName() {
		return null;
	}
	
	// 계정 만료 여부 확인 (true: 만료x, false: 만료o => 사용 불가)
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	// 계정 잠금 여부 확인 (true: 잠금x, false: 잠금o => 사용 불가)
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	// 비밀번호 만료 여부 확인 (true: 만료x, false: 만료o => 사용 불가)
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	/*
	 * 계정 활성화 여부 확인 (true: 활성화, false: 활성화x => 사용 불가)
	 * ex) 1년간 로그인 기록이 없을 시 휴면계정으로 전환할 때 사용
	 * 현재시간 - 최근 로그인 시간 > 1년 => 휴면계정
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}

	// 계정이 가진 권한 목록을 리턴(권한이 여러개일 시 for문으로 반복을 통해 해줘야 함)
	@SuppressWarnings("serial")
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			
			@Override
			public String getAuthority() {
				return "ROLE_" + member.getRole();
			}
		});
		
		return collect;
	}

	@Override
	public Map<String, Object> getAttributes() {
		return attributes;
	}
}
