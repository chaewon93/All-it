package com.ezen.allit;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.ezen.allit.domain.Role;
import com.ezen.allit.domain.Seller;
import com.ezen.allit.repository.SellerRepository;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class SellerTest {
	@Autowired
	private SellerRepository sellerRepo;
	
	@Test
	public void insert() {
		for(int i=1; i<=5; i++) {
			Seller seller = new Seller();
			seller.setId("a"+i);
			seller.setPwd("a"+i);
			seller.setName("판매자"+i);
			seller.setEmail("seller@seller.com");
			seller.setPhone("010-1111-1111");
			seller.setAddress("서울시 관악구 신림동");
			seller.setZipcode("111-111");
			seller.setRegno("1111-33-22-8");
			seller.setContent("갓김치를 파는 사람입니다.");
			seller.setRole(Role.SELLER);
			sellerRepo.save(seller);
		}
	}
}
