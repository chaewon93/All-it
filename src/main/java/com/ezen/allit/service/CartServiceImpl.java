package com.ezen.allit.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
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

	@Override
	public List<Cart> getCartList(Cart cart) {
		return cartRepo.findAll(Sort.by(Sort.Direction.DESC, "cno"));
		
	}

}
