package com.ezen.allit.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = {"product", "seller", "member"})
@Entity
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int rvno;
	private String content;
	private String imageName;
	private int rating;
	private int hit;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "pno")
	private Product product;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "sid")
	private Seller seller;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "mid")
	private Member member;
	@CreationTimestamp
	private Date regDate;
}
