package com.ezen.allit.config.oauth.provider;

import java.util.Map;

/*
 * OAuth2에 필수적인 provier와 providerId 정보를 변수로 가진 인터페이스를 생성해
 * KakaoUserInfo가 인터페이스를 구현하는 방식으로 해서 이지코딩
 */
public class KakaoUserInfo implements OAuth2UserInfo {
	private Map<String, Object> attributes;// oAuth2User.getAttributes()

	public KakaoUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
	}
	
    public Map<String, Object> getAttributes() {
        return attributes;
    }
	
	@Override
	public String getProvider() {
		return "kakao";
	}

	@Override
	public String getProviderId() {
		return (String) attributes.get("id");
	}

	@Override
	public String getEmail() {
		return (String) attributes.get("email");
	}

	@Override
	public String getName() {
		return (String) attributes.get("name");
	}

}
