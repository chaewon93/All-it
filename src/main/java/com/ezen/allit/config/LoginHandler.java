//package com.ezen.allit.config;
//
//import java.io.IOException;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.HttpSession;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
//
//import com.ezen.allit.repository.MemberRepository;
//import com.ezen.allit.repository.SellerRepository;
//
//public class LoginHandler extends SimpleUrlAuthenticationSuccessHandler {
//	@Autowired
//	private MemberRepository memberRepo;
//	@Autowired
//	private SellerRepository sellerRepo;
//	
//	@Override
//	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
//			Authentication authentication) throws IOException, ServletException {
//		
//		setDefaultTargetUrl("/member-login");
//		
//		HttpSession session = request.getSession();
//		
//		if(session != null) {
//			String name = authentication.getName();
//			System.out.println("name = " + name);
//			session.getAttribute("")
//			
//			if()
//		}
//	}
//}
