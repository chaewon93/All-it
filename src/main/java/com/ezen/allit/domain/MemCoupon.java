package com.ezen.allit.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = "member")
@Entity
public class MemCoupon {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int mcid;			// 사용자 쿠폰 번호
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="mid")
	private Member member;		// 양방향 다대다 설정-> 다대일 두개로 나눔
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="couId")
	private Coupon coupon;		// 양방향 다대다 설정-> 다대일 두개로 나눔
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createMemCouDate = new Date();		// 쿠폰 생성일
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endMemCouDate;		// 쿠폰 만료일
	private int status;				// 사용 여부 0=미사용, 1=사용, 2=만료
}
