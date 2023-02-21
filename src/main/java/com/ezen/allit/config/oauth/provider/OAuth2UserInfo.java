package com.ezen.allit.config.oauth.provider;

/*
 * OAuth2에 필수적인 provier와 providerId 정보를 변수로 가진 인터페이스를 생성해
 * 로그인 플랫폼들이 이 인터페이스를 구현하는 방식으로 해서 이지코딩
 */
public interface OAuth2UserInfo {
	String getProvider(); // 플랫폼명
	String getProviderId(); // 플랫폼 자체에서 사용자를 저장하는 id 값 -> 대부분 123456456489789asddas와 같은 난수
	String getEmail(); // 플랫폼 가입 시 제출한 email
	String getName();
}
