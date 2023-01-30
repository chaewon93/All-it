package com.ezen.allit.apicontroller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ezen.allit.domain.Product;
import com.ezen.allit.service.ProductService;
import com.ezen.allit.service.SellerService;

import dto.ResponseDto;

@RestController
public class SellerApiController {
	@Autowired
	private SellerService sellerService;
	@Autowired
	private ProductService productService;
	
	/*
	 * 상품삭제
	 */
	@DeleteMapping("/seller/delete/{pno}")
	public ResponseDto<Integer> deleteProduct(@PathVariable int pno) {
		productService.deleteProduct(pno);
		
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












