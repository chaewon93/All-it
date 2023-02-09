package com.ezen.allit.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(exclude = "memCoupon")
@Entity
public class Coupon {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int couId;				// 쿠폰 번호
	private String couName;			// 쿠폰 이름(난수 문자열)
	private String couContent;		// 쿠폰 설명
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createDate = new Date();		// 쿠폰 생성일
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date endDate;			// 쿠폰 종료일
	private int period;				// 기간
	private String regId;			// 발행인
	private int discount;			// 할인금액 or 할인율
	private int minPrice;			// 사용 최소 금액
	private int maxValue;			// 최대 할인 금액
	@OneToMany(mappedBy = "coupon", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
	@JsonIgnoreProperties({"coupon"})
	private List<MemCoupon> memCoupon = new ArrayList<>();
	private String condition;		// 사용 제한 조건
	
	@Override
	public boolean equals(Object obj) {

		if(obj instanceof Coupon) {
			Coupon temp = (Coupon)obj;
			if(this.couId == temp.getCouId()) {
				return true;
			}
		}
		
		return false;
	}
	
}
