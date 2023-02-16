package com.ezen.allit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;

import com.ezen.allit.config.oauth.PrincipalOauth2UserService;

import lombok.RequiredArgsConstructor;

/*
 * OAuth2 Client을 사용하면 액세스 토큰과 사용자 정보 두 가지를 한 번에 다 받아옴 - 한번에 끝
 */
@RequiredArgsConstructor
@EnableWebSecurity // 빈 등록 역할, 시큐리티 활성화(스프링 시큐리티 필터가 필터체인에 등록)
@EnableGlobalMethodSecurity(securedEnabled = true) // 특정 주소로 접근할 때 권한/인증을 미리 체크, securedEnabled=true: @secured를 활성화
public class SecurityConfig {
	private final PrincipalOauth2UserService principalOauth2UserService; 
	
	/*
	 * HttpSecurity: 시큐리티 설정을 하는 주체
	 * HttpSecurity: url(리소스) 접근 권한, 인증 후 이동페이지, 커스텀 필터 등 외에도 거의 모든 시큐리티 설정을 담당
	 * 
	 * csrf(Cross Site Request Forgery): 자신의 의도와 상관없이 웹 사이트를 공격하는 행위
	 * (1) csrf는 스프링 시큐리티 사용 시 자동 발동 => uuid로 csrfToken을 생성해 세션에 유저정보와 함께 넘겨줌
	 * (2) 서버단에서 유저 로그인 시 csrfToken이 있는지 없는지 여부로 정상 로그인인지 아닌지를 확인!!
	 */
	@Bean
	SecurityFilterChain memberFilterChain(HttpSecurity http) throws Exception {
		http.csrf().disable(); 							   // csrf 필터 해제
		http.authorizeRequests() 						   // authorizeRequests(): 요청에 관한 지정
			.antMatchers("/**", "/auth/**", "/product/**", "/css/**", "/img/**", "file:///c:/fileUpload/images/**")
			.permitAll()  								   // permitAll(): 모두 허가
//			.antMatchers("/member/**").hasRole("MEMBER")   // antMatchers(): 특정 url에 대한 권한 설정
			.antMatchers("/seller/**").hasRole("SELLER")   // antMatchers(): 특정 url에 대한 권한 설정
			.antMatchers("/admin/**").hasRole("ADMIN")	   // hasRole(): 특정 권한만 접근 가능
			.anyRequest() 								   // anyRequest(): 외의 모든 요청을
			.authenticated()							   // authenticated(): 로그인 인증이 필요
			.and()
			.formLogin()  								   // formLogin(): form 태그 기반의 로그인을 지원
			.loginPage("/member-login") 			       // loginPage(): 로그인 페이지 지정
			.loginProcessingUrl("/member/login")	   	   // loginProcessingUrl(): 해당 url 시큐리티 로그인이 처리
//			.successHandler(new LoginSuccessHandler())
			.defaultSuccessUrl("/")			  			   // defaultSuccessUrl(): 로그인 성공 시 이동할 url
			.failureUrl("/member-login")			  	   // failureUrl(): 로그인 실패 시 이동할 url
			.usernameParameter("id")					   // usernameParameter(): username로 고정된 파라미터 속성명 변경
			.passwordParameter("pwd")					   // passwordParameter(): password로 고정된 파라미터 속성명 변경
			.and()
			.oauth2Login()								   // oauth2Login(): OAuth 로그인일 경우
			.loginPage("/member-login")
			.userInfoEndpoint()							   // userInfoEndpoint(): 사용자 정보 가져올 때 사용
			.userService(principalOauth2UserService);	   // userService(): 파라미터로 처리
		http.logout()									   // logout(): 로그아웃에 관한 설정
			.logoutUrl("/logout")						   // logoutUrl(): 해당 url 시큐리티 로그아웃이 처리 
			.logoutSuccessUrl("/");				  		   // logoutSuccessUrl(): 로그아웃 성공 시 이동할 url
		http.exceptionHandling().accessDeniedHandler(accessDeniedHandler());	
		
		return http.build();
	}
	
//	@Order(2)
//	@Bean
//	SecurityFilterChain sellerFilterChain(HttpSecurity http) throws Exception {
//		http.csrf().disable(); 							   // csrf 필터 해제
//		http.exceptionHandling()
//			.accessDeniedPage("/sellerLogin"); 			   // 권한이 필요한 페이지를 요청할 경우
//		http.authorizeRequests() 						   // authorizeRequests(): 요청에 관한 지정
//			.antMatchers("/**", "/auth/**", "/product/**", "/css/**", "/img/**", "file:///c:/fileUpload/images/**")
//			.permitAll()  								   // permitAll(): 모두 허가
//			.antMatchers("/seller/**").hasRole("SELLER")   // antMatchers(): 특정 url에 대한 권한 설정
//			.antMatchers("/admin/**").hasRole("ADMIN")	   // hasRole(): 특정 권한만 접근 가능
//			.anyRequest() 								   // anyRequest(): 외의 모든 요청을
//			.authenticated()
//			.and()
//			.formLogin()  								   // formLogin(): form 태그 기반의 로그인을 지원
//			.loginPage("/sellerLogin") 			       	   // loginPage(): 로그인 페이지 지정
//			.loginProcessingUrl("/seller/login")	   	   // loginProcessingUrl(): 해당 url 시큐리티 로그인이 처리 
//			.defaultSuccessUrl("/seller/")			   	   // defaultSuccessUrl(): 로그인 성공 시 이동할 url
//			.failureUrl("/sellerLogin")			  	  	   // failureUrl(): 로그인 실패 시 이동할 url
//			.usernameParameter("id")					   // usernameParameter(): username로 고정된 파라미터 속성명 변경
//			.passwordParameter("pwd");					   // passwordParameter(): password로 고정된 파라미터 속성명 변경
//		http.logout()									   // logout(): 로그아웃에 관한 설정
//			.logoutUrl("/seller/logout")				   // logoutUrl(): 해당 url 시큐리티 로그아웃이 처리 
//			.logoutSuccessUrl("/");				  		   // logoutSuccessUrl(): 로그아웃 성공 시 이동할 url
//			
//		return http.build();
//	}
	
	/*
	 * 비밀번호 해쉬화
	 */
	// @Bean: 해당 매서드의 리턴 오브젝트를 IoC로 등록해줌
	@Bean
	@Lazy
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
	/*
	 * (정보수정 후) 시큐리티 세션 재주입
	 */
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	/*
	 * accessDeniedHandler
	 */
    private AccessDeniedHandler accessDeniedHandler() {
        CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();
        accessDeniedHandler.setErrorPage("/denied");
        return accessDeniedHandler;
      }
}





