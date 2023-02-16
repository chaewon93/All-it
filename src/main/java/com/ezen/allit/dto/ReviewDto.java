package com.ezen.allit.dto;

import java.util.Date;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.ezen.allit.domain.Member;
import com.ezen.allit.domain.OrdersDetail;
import com.ezen.allit.domain.Product;
import com.ezen.allit.domain.Seller;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
	private int rvno;
	private Member member;
	private String mid;
	private Seller seller;
	private String sid;
	private Product product;
	private int pno;
	private OrdersDetail ordersDetail;
	private int odno;
	private String content;
	private int rating;
	private List<MultipartFile> imageFile;
	private List<String> imageName;
	private int fileStatus;
	private Date regDate;
	
//	public static ReviewDto toReviewDto(Review review) {
//		ReviewDto reviewDto = new ReviewDto();
//		reviewDto.setRvno(review.getRvno());
//		reviewDto.setMember(review.getMember());
//		reviewDto.setSeller(review.getSeller());
//		reviewDto.setProduct(review.getProduct());
//		reviewDto.setOrdersDetail(review.getOrdersDetail());
//		reviewDto.setContent(review.getContent());
//		reviewDto.setRating(review.getRating());
//		reviewDto.setRegDate(review.getRegDate());
//		if(review.getFileStatus() == 0) {
//			reviewDto.setFileStatus(review.getFileStatus());
//		} else {
//			List<String> imageName = new ArrayList<>();
//			reviewDto.setFileStatus(review.getFileStatus());
//			
//			for(ReviewFile reviewFile : review.getReviewFile()) {				
//				imageName.add(reviewFile.getImageName());
//			}
//			reviewDto.setImageName(imageName);
//		}
//		
//		return reviewDto;
//	}
}
