package com.ezen.allit.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.allit.domain.Cart;
import com.ezen.allit.repository.CartRepository;

@Service
public class CartServiceImpl implements CartService {
	
	@Autowired
	private CartRepository cartRepo;

	@Override
	@Transactional
	public void insertCart(Cart cart) {
		cartRepo.save(cart);
	}

}
