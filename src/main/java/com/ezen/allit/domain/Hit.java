package com.ezen.allit.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
@NoArgsConstructor
public class Hit {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int hno;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "pno")
	private Product product;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rvno")
	private Review review;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "sid")
	private Seller seller;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "mid")
	private Member member;
	
	public Hit(Product product, Seller seller) {
		this.product = product;
		this.seller = seller;
	}
	
	public Hit(Review review, Seller seller) {
		this.review = review;
		this.seller = seller;
	}	
}
