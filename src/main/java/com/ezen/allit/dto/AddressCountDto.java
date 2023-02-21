package com.ezen.allit.dto;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class AddressCountDto {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	int seq;
	String address;
	int count;
}
