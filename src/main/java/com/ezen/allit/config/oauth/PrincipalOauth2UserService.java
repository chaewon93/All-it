package com.ezen.allit.config.oauth;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.ezen.allit.config.auth.PrincipalDetailMember;
import com.ezen.allit.config.oauth.provider.FacebookUserInfo;
import com.ezen.allit.config.oauth.provider.GoogleUserInfo;
import com.ezen.allit.config.oauth.provider.KakaoUserInfo;
import com.ezen.allit.config.oauth.provider.NaverUserInfo;
import com.ezen.allit.config.oauth.provider.OAuth2UserInfo;
import com.ezen.allit.domain.Grade;
import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.Role;
import com.ezen.allit.repository.MemberRepository;

/*
 * 시큐리티가 OAuth 로그인을 처리할 때(oauth2Login()이 실행되면)
 * DefaultOAuth2UserService 타입으로 IoC 되어 있는 loadUser 매서드가 실행됨
 * 여기는 username만 처리하는 곳
 * 즉, OAuth 로그인을 처리하는 곳
 */
@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	@Autowired
	private MemberRepository memberRepo;
	String kakaoProviderId = "";
	String kakaoUsername = "";
	String realName = "";

	/*
	 * 1. 구글 로그인 클릭
	 * 2. 구글 로그인창 이동, 로그인 후 코드 리턴(OAuth2 Client라이브러리가 해줌)
	 * 3. 받은 코드로 액세스 토큰 요청 => userRequest 정보
	 * 4. userRequest 정보로 loadUser 매서드를 호출해 회원정보 받아오기
	 * 
	 * 구글로부터 받은 OAuth2UserRequest 데이터로 로그인하는 매서드
	 * 그리고 해당 매서드가 종료될 때 @AuthenticationPrincipal 어노테이션이 생성
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws  OAuth2AuthenticationException {
		System.out.println("userRequest: " + userRequest.getClientRegistration()); // registerationId를 통해 어느 sns 로그인인지 확인 가능
		System.out.println("getAccessToken: " + userRequest.getAccessToken());
		
		OAuth2User oAuth2User = super.loadUser(userRequest);
		System.out.println("getAttributes: " + oAuth2User.getAttributes());
		
		// 구글로부터 받은 정보로 사이트에 강제로 회원가입 진행
		OAuth2UserInfo oAuth2UserInfo = null;
		if(userRequest.getClientRegistration().getRegistrationId().equals("google")) {
			System.out.println("구글 로그인 요청");
			realName = oAuth2User.getAttributes().get("name").toString();
			oAuth2UserInfo = new GoogleUserInfo(oAuth2User.getAttributes());
			
		} else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
			System.out.println("페이스북 로그인 요청");
			realName = oAuth2User.getAttributes().get("name").toString();
			oAuth2UserInfo = new FacebookUserInfo(oAuth2User.getAttributes());	
			
		} else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")) {
			System.out.println("네이버 로그인 요청");
			LinkedHashMap list = (LinkedHashMap) oAuth2User.getAttributes().get("response");
			realName = list.get("name").toString();
			oAuth2UserInfo = new NaverUserInfo((Map<String, Object>) oAuth2User.getAttributes().get("response"));
			
		} else if(userRequest.getClientRegistration().getRegistrationId().equals("kakao")) {
			System.out.println("카카오 로그인 요청");
			LinkedHashMap list = (LinkedHashMap) oAuth2User.getAttributes().get("properties");
			realName = list.get("nickname").toString();
			kakaoProviderId = oAuth2User.getAttributes().get("id").toString();
			kakaoUsername = "kakao_"+oAuth2User.getAttributes().get("id").toString();
			oAuth2UserInfo = new KakaoUserInfo((Map<String, Object>) oAuth2User.getAttributes().get("kakao_account"));
		}
		
		String provider = oAuth2UserInfo.getProvider(); // 플랫폼명
		String providerId = oAuth2UserInfo.getProviderId(); // 플랫폼 자체에서 사용자를 저장하는 id 값 -> 대부분 123456456489789asddas와 같은 난수
		String username = provider + "_" + providerId; // sns로 부터 받은 정보로 사이트 측에서 임의로 회원가입을 시켜줘야 하는데 이때 사용할 id
		String password = encoder.encode("1234"); // 마찬가지로 임의로 지정할 pwd -> 절대 외부에 공개돼서는 안됨
		String email = oAuth2UserInfo.getEmail(); // sns 가입 시 제출한 email
		System.out.println("email = " + email);
		
		Member member = memberRepo.findById(username).orElse(null); // sns로그인시 id를 매개로 DB에서 가입기록을 조회
		if(member == null) { // 기존 가입기록이 없으면 회원가입 진행, 없으면 곧바로 로그인
			System.out.println(provider + " 플랫폼으로 최초 로그인 시도로 회원가입을 진행합니다.");
			if(provider.equals("kakao")) {
				member = Member.builder()
						.id(kakaoUsername)
						.pwd(password)
						.email(email)
						.name(realName)
						.role(Role.MEMBER)
						.grade(Grade.BRONZE)
						.provider(provider)
						.providerId(kakaoProviderId)
						.build();			
				memberRepo.save(member);
			} else {
				member = Member.builder()
						.id(username)
						.pwd(password)
						.email(email)
						.name(realName)
						.role(Role.MEMBER)
						.grade(Grade.BRONZE)
						.provider(provider)
						.providerId(providerId)
						.build();			
				memberRepo.save(member);
			}
		}
		
		System.out.println(provider + " 플랫폼으로 로그인 진행합니다.");
		return new PrincipalDetailMember(member, oAuth2User.getAttributes());
	}
}
