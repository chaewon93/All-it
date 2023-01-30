package com.ezen.allit.service;

import java.util.List;

import com.ezen.allit.domain.CustomerCenter;

public interface CustomerCenterService {

	List<CustomerCenter> getCustomercenter();
	
	void insertCustomerCenter(CustomerCenter cus);
	
	void updateCusto(int cno);
	
	void deleteCusto(int cno);
	
}
