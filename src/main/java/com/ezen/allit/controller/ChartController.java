package com.ezen.allit.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.persistence.Entity;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ezen.allit.dto.AddressCount;
import com.ezen.allit.dto.AddressCountDto;
import com.ezen.allit.repository.MemberRepository;
import com.ezen.allit.service.MemberService;

@Controller
@RequestMapping("/chart/")
public class ChartController {
	
	@Autowired
	private MemberRepository memRepo;
	
	@Autowired
	private MemberService memService;
	
	// 메인 차트
	@RequestMapping("/mainCharts")
	public String salesRecorddChart(Model model){
		// 성별
		String m = "남자";
		String w = "여자";
		String x = "안함";
		int mc = memRepo.countMemberByGender(m);
		int wc = memRepo.countMemberByGender(w);
		int xc = memRepo.countMemberByGender(x);
		
		model.addAttribute("m", mc);
		model.addAttribute("w", wc);
		model.addAttribute("x", xc);
		
		// 주소
//		Map<String, Integer> mapAddress = memRepo.chartAddressGroup();
//		List<Map<String, Object>> mapAddress = memRepo.chartAddressGroup();
//		LinkedHashMap<String, Object> mapAddress = memRepo.chartAddressGroup();
//		List<Object[]> mapAddress = memRepo.chartAddressGroup();
		
//		List<AddressCountDto> addressCount = memService.getListAddressCount();
//		System.out.println("==================================== 주소 차트");
//		System.out.println(addressCount);
		
//		Map<String, String> mapAdd = new HashMap<>();
//		Iterator<Map<String,Object>> iter = mapAddress.iterator();
			
//		System.out.println(mapAddress);
//		for(Object[] add : mapAddress) {
//			System.out.println(add);
//		}
//		
//		model.addAttribute("mapAddress", mapAddress);
		
//		List<AddressCount> addressCountList = memRepo.chartAddressGroup();
//		Map<String, Integer> mapAdd = new HashMap<>();
//		List<String> listAdd = new ArrayList<>();
//		List<Integer> listCount = new ArrayList<>();
//		System.out.println("============================== 주소 차트");
//		for(AddressCount addressCount : addressCountList) {
//			System.out.println(addressCount);
//			System.out.println(addressCount.getAddress());
//			System.out.println(addressCount.getAddressCount());
//			
//			mapAdd.put(addressCount.getAddress(), addressCount.getAddressCount());
//			listAdd.add(addressCount.getAddress());
//			listCount.add(addressCount.getAddressCount());
//		}
//		System.out.println(mapAdd);
//		model.addAttribute("mapAdd", mapAdd);
//		
//		Set<Entry<String, Integer>> add = mapAdd.entrySet();
//		System.out.println(add);
//		model.addAttribute("setAdd", add);
//		
//		model.addAttribute("listAdd", listAdd);
//		model.addAttribute("listCount", listCount);
		return "/admin/mainCharts";
	}

}

