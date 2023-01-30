package com.ezen.allit.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.allit.domain.CustomerCenter;
import com.ezen.allit.repository.CustomerCenterRepository;

@Service
public class CustomerCenterServiceImpl implements CustomerCenterService {
	
	@Autowired
	CustomerCenterRepository cusRepo;

	@Override
	public List<CustomerCenter> getCustomercenter() {
		List<CustomerCenter> custoList = cusRepo.findAll();
		if(!custoList.isEmpty())
			return custoList;
		else return null;
	}

	@Override
	public void insertCustomerCenter(CustomerCenter cus) {

		CustomerCenter custo = new CustomerCenter();
		
		custo.setCategory(cus.getCategory());
		custo.setTitle(cus.getTitle());
		custo.setContent(cus.getContent());
		
		cusRepo.save(custo);		
	}

	@Override
	public void updateCusto(CustomerCenter cus) {
		
		CustomerCenter custo = cusRepo.findCustomerCenterByCno(cus.getCno());
		
		custo.setCategory(cus.getCategory());
		custo.setTitle(cus.getTitle());
		custo.setContent(cus.getContent());
		
		cusRepo.save(custo);		
	}

	@Override
	public void deleteCusto(int cno) {

		cusRepo.deleteById(cno);		
	}

}
