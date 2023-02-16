package com.ezen.allit.config;

import java.io.IOException;
import java.util.Optional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.ezen.allit.domain.Member;
import com.ezen.allit.repository.MemberRepository;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
	@Autowired
	private MemberRepository memberRepository;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		System.out.println("authentication = " + authentication.getName());
		Optional<Member> member = memberRepository.findById(authentication.getName());
		System.out.println("mem = " + member);
		
		HttpSession session = request.getSession();
		session.setAttribute("memberDto", member);
		
		clearSession(request);
		
		response.sendRedirect("/");
	}
	
	protected void clearSession(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		if(session == null) return;
		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
	}
	
}
