package com.ezen.allit.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ezen.allit.dto.Chart;
import com.ezen.allit.repository.MemberRepository;
import com.ezen.allit.repository.ProductRepository;

@Controller
@RequestMapping("/chart/")
public class ChartController {
	
	@Autowired
	private MemberRepository memRepo;
	
	@Autowired
	private ProductRepository proRepo;
	
	// 메인 차트
	@GetMapping("/mainChart")
	public String testChartView(Model model){
		
		return "/admin/mainCharts";
	}
	
	// 성별
	@PostMapping("/mainChartGender")
	@ResponseBody
	public List<Map<String, Object>> genderChart(){
		
		// chart 형식(이름과 카운트)으로 성별 조회(성별-수)
		List<Chart> gender = memRepo.chartGenderGroup();
		List<Map<String, Object>> mapList = new ArrayList<>();

		// 얻어온 값을 map에 담아서 전달
		for(Chart gen : gender) {
			Map<String, Object> map = new HashMap<>();
			map.put("gender",gen.getGender());
			map.put("genderCount", gen.getCount());
			mapList.add(map);
		}
		
		return mapList;
	}
	
	// 주소
	@PostMapping("/mainChartAddress")
	@ResponseBody
	public List<Map<String, Object>> addressChart(){
		
		List<Chart> address = memRepo.chartAddressGroup();
		List<Map<String, Object>> mapList = new ArrayList<>();

		for(Chart add : address) {
			Map<String, Object> map = new HashMap<>();
			map.put("address",add.getAddress());
			map.put("addressCount", add.getCount());
			mapList.add(map);
		}
		
		return mapList;
	}
	
	// 등급
	@PostMapping("/mainChartGrade")
	@ResponseBody
	public List<Map<String, Object>> gradeChart(){
		
		List<Chart> grade = memRepo.chartGradeGroup();
		List<Map<String, Object>> mapList = new ArrayList<>();

		for(Chart gra : grade) {
			Map<String, Object> map = new HashMap<>();
			map.put("grade",gra.getGrade());
			map.put("count", gra.getCount());
			mapList.add(map);
		}
		
		return mapList;
	}	
	
	// 카테고리
	@PostMapping("/mainChartCategory")
	@ResponseBody
	public List<Map<String, Object>> categoryChart(){
		
		List<Chart> category = proRepo.chartCategoryGroup();
		List<Map<String, Object>> mapList = new ArrayList<>();

		for(Chart cate : category) {
			Map<String, Object> map = new HashMap<>();
			// 카테고리 번호마다 이름 적용
			switch(cate.getCategory()) {
				case "1" :
					map.put("category","패션");
					map.put("count", cate.getCount());
					break;
				case "2" :
					map.put("category","식품");
					map.put("count", cate.getCount());
					break;
				case "3" :
					map.put("category","주방용품");
					map.put("count", cate.getCount());
					break;
				case "4" :
					map.put("category","생활용품");
					map.put("count", cate.getCount());
					break;
				case "5" :
					map.put("category","인테리어");
					map.put("count", cate.getCount());
					break;
				case "6" :
					map.put("category","가전제품");
					map.put("count", cate.getCount());
					break;
				case "7" :
					map.put("category","스포츠/레저");
					map.put("count", cate.getCount());
					break;
				case "8" :
					map.put("category","도서/음반/DVD");
					map.put("count", cate.getCount());
					break;
				case "9" :
					map.put("category","반려동물용품");
					map.put("count", cate.getCount());
					break;
				case "10" :
					map.put("category","건강식품");
					map.put("count", cate.getCount());
					break;
			}
			mapList.add(map);
		}
		
		return mapList;
	}	

}

