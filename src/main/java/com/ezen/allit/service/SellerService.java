package com.ezen.allit.service;

import com.ezen.allit.domain.Seller;

public interface SellerService {

	public Seller findByIdAndPwd(String id, String pwd);
	
	void saveSeller(Seller seller);
	
	Seller getSeller(Seller seller);

}
