package com.ezen.allit.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.ezen.allit.domain.Cart;
import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.Product;
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
	public List<Cart> getCartList(Member member) {
		return cartRepo.findByMember(member);
	}

	@Override
	public void deleteCart(int cno) {
		cartRepo.deleteById(cno);
	}

	@Override
	public Cart checkCart(Member member, Product product) {
		return cartRepo.findByMemberAndProduct(member, product);
	}

}
