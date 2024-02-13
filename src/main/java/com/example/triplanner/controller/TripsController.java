package com.example.triplanner.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.triplanner.entity.Prefecture;
import com.example.triplanner.entity.Region;
import com.example.triplanner.entity.Tag;
import com.example.triplanner.form.TripsNewForm;
import com.example.triplanner.repository.PrefectureRepository;
import com.example.triplanner.repository.RegionRepository;
import com.example.triplanner.repository.TagRepository;

@Controller
@RequestMapping("/trips")
public class TripsController {

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private RegionRepository regionRepository;

	@Autowired
	private PrefectureRepository prefectureRepository;

	//旅一覧画面
	@GetMapping("/index")
	public String index(Model model) {
		List<Tag> tags = tagRepository.findAllByOrderById();
		model.addAttribute("tags", tags);

		List<Region> regions = regionRepository.findAllByOrderById();
		model.addAttribute("regions", regions);

		List<Prefecture> prefectures = prefectureRepository.findAllByOrderById();
		model.addAttribute("prefectures", prefectures);

		return "trips/index";
	}

	//旅作成画面
	@GetMapping("/new")
	public String newTrip(Model model) {
		System.out.println("new");
		return "trips/new";
	}


//	//旅程作成画面
//	@PostMapping("/itinerary")
//	public String newItinerary(@RequestBody List<Map<String, String>> tableData, Model model) {
//		System.out.println("旅程作成画面コントローラ");
//		System.out.println(tableData);
//		return "trips/itinerary";
//	}
	
	//旅程作成画面
	@PostMapping("/itinerary")
	public String newItinerary(TripsNewForm tripsNewForm, Model model) {
		System.out.println("旅程作成画面コントローラ");
		model.addAttribute("tripsNewForm", tripsNewForm);
		return "trips/itinerary";
	}

	//旅程確認登録画面
	@GetMapping("/confirm")
	public String newItineraryConfirm(Model model) {
		return "trips/confirm";
	}

}
