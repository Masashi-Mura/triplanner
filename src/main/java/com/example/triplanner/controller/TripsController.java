package com.example.triplanner.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.triplanner.entity.Prefecture;
import com.example.triplanner.entity.Region;
import com.example.triplanner.entity.Tag;
import com.example.triplanner.repository.PrefectureRepository;
import com.example.triplanner.repository.RegionRepository;
import com.example.triplanner.repository.TagRepository;

@Controller
public class TripsController {

	@Autowired
	private TagRepository tagRepository;
	
	@Autowired
	private RegionRepository regionRepository;
	
	@Autowired
	private PrefectureRepository prefectureRepository;
	
	//GetMappingの"/trips"は開発時の仮名称
	@GetMapping("/trips")  
	public String index(Model model) {
		List<Tag> tags = tagRepository.findAllByOrderById();
		model.addAttribute("tags", tags);
		
		List<Region> regions = regionRepository.findAllByOrderById();
		model.addAttribute("regions", regions);
		
		List<Prefecture> prefectures = prefectureRepository.findAllByOrderById();
		model.addAttribute("prefectures", prefectures);
		
		System.out.println("test");
		
		return "trips/index";
	}
	
}
