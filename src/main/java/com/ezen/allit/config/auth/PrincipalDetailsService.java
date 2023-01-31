//package com.ezen.allit.config.auth;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import com.ezen.allit.domain.Seller;
//import com.ezen.allit.repository.SellerRepository;
//
///*
// * 시큐리티가 로그인을 처리할 때 (즉, http.loginProcessingUrl이 실행될 때)
// * UserDetailsService 타입으로 IoC 되어 있는 loadUserByUsername 매서드가 실행됨
// * 여기는 username만 처리하는 곳
// */
//@Service
//public class PrincipalDetailsService implements UserDetailsService {
//	@Autowired
//	private SellerRepository sellerRepo;
//	
//	/*
//	 * UserDetails 타입으로 시큐리티 세션 저장소에 유저정보가 들어감
//	 * 그리고 해당 매서드가 종료될 때 @AuthenticationPrincipal 어노테이션이 생성
//	 */
//	@Override
//	public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
//		Seller seller = sellerRepo.findById(id).get();
//		
//		if(seller != null) {
//			return new PrincipalDetails(seller);
//		} else {
//			return null;
//		}
//	}
//
//}
