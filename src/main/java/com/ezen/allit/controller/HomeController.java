package com.ezen.allit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
	
	// 홈 화면 이동
	@GetMapping({"", "/"})
	public String index() {
		
		return "index";
	}	
}
