//package com.ezen.allit.config;
//
//import java.io.IOException;
//import java.util.Optional;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.WebAttributes;
//import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
//
//import com.ezen.allit.domain.Member;
//import com.ezen.allit.domain.Seller;
//import com.ezen.allit.repository.MemberRepository;
//import com.ezen.allit.repository.SellerRepository;
//
//public class LoginSuccessHandler implements AuthenticationSuccessHandler {
//	@Autowired
//	private MemberRepository memberRepo;
//	@Autowired
//	private SellerRepository sellerRepo;
//
//	@Override
//	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//			Authentication authentication) throws IOException, ServletException {
//		System.out.println("authentication.getName() = " + authentication.getName());
//		Optional<Member> member = memberRepo.findById(authentication.getName());
//		System.out.println("mem = " + member);
//		HttpSession session = request.getSession();
//		session.setAttribute("memberDto", member);
//		
////		Optional<Seller> seller = sellerRepo.findById(authentication.getName());
////		System.out.println("seller = " + seller);
//		
//		clearSession(request);
//		
//		response.sendRedirect("/");
//	}
//	
//	protected void clearSession(HttpServletRequest request) {
//		HttpSession session = request.getSession(false);
//		if(session == null) return;
//		session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
//	}
//	
//}
