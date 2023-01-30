package com.ezen.allit.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ezen.allit.domain.Product;
import com.ezen.allit.dto.ResponseDto;
import com.ezen.allit.dto.ReviewSaveRequestDto;
import com.ezen.allit.service.ProductService;
import com.ezen.allit.service.ReviewService;
import com.ezen.allit.service.SellerService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class SellerApiController {
	private final SellerService sellerService;
	private final ReviewService reviewService;
	private final ProductService productService;
	
	/*
	 * 상품삭제
	 */
	@DeleteMapping("/seller/product/delete/{pno}")
	public ResponseDto<Integer> deleteProduct(@PathVariable int pno) {
		sellerService.deleteProduct(pno);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	/*
	 * 리뷰작성
	 */
	@PostMapping("/review/save/{pno}")
	public ResponseDto<Integer> saveReview(@RequestBody ReviewSaveRequestDto reviewSaveRequestDto) {
		reviewService.saveReview(reviewSaveRequestDto);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	
	/*
	 * 상품수정
	 */
//	@PostMapping("/seller/modify/{pno}")
//	public ResponseDto<Integer> modifyProduct(@PathVariable int pno,
//											@RequestBody Product product, MultipartFile imageFile) throws Exception {
//		productService.modifyProduct(pno, product, imageFile);
//		
//		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1); 
//	}
}











