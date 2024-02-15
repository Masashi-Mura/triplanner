package com.example.triplanner.controller;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.triplanner.entity.Prefecture;
import com.example.triplanner.entity.Region;
import com.example.triplanner.entity.Tag;
import com.example.triplanner.form.ItineraryForm;
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
		//出発時間、滞在時間、場所の初期値をmodelに設定
		TripsNewForm tripsNewForm = new TripsNewForm();
		List<LocalDateTime> departTimes = new ArrayList<>();
		departTimes.add(LocalDateTime.of(2020, 1, 1, 8, 0, 0));
		tripsNewForm.setDepartTimes(departTimes);
		List<LocalTime> stayTimes = new ArrayList<>();
		stayTimes.add(LocalTime.of(1, 30));
		stayTimes.add(LocalTime.of(2, 0));
		tripsNewForm.setStayTimes(stayTimes);
		List<String> placeNames = new ArrayList<>();
		placeNames.add("トヨタレンタカー大阪駅前");
		placeNames.add("興福寺");
		placeNames.add("平城宮跡歴史公園");
		placeNames.add("トヨタレンタカー大阪駅前");
		tripsNewForm.setPlaceNames(placeNames);
		model.addAttribute("tripsNewForm", tripsNewForm);

		return "trips/new";
	}

	//旅程作成画面
	@PostMapping("/itinerary")
	public String newItinerary(@Validated TripsNewForm tripsNewForm,
			BindingResult result, Model model) {
		System.out.println("旅程作成画面コントローラ");
		if (result.hasErrors()) {
			//出発時刻をLocalDateからLocalDateTimeに変換
			LocalDateTime enteredDepartTime = LocalDateTime.of(LocalDate.of(2020, 1, 1),
					tripsNewForm.getEnteredDepartTimeValue());
			tripsNewForm.setDepartTimes(List.of(enteredDepartTime));
			model.addAttribute("tripsNewForm", tripsNewForm);
			//モデルにエラーの設定
			model.addAttribute("hasMessage", true);
			model.addAttribute("message", "ルート検索を行い時間を設定してください。");
			return "trips/new";
		}

		// tripsNewFormをitineraryFormに変換
		List<Integer> rowSequence = new ArrayList<>();
		List<LocalDateTime> startTime = new ArrayList<>();
		List<LocalDateTime> endTime = new ArrayList<>();
		List<String> departureName = new ArrayList<>();
		List<String> arrivalName = new ArrayList<>();
		int arrivalTimesLnegth = tripsNewForm.getArrivalTimes().size();
		for (int i = 0; i < arrivalTimesLnegth; i++) {
			//偶数行
			rowSequence.add(i * 2);
			System.out.println("i=" + i + "の偶数行計算開始");
			startTime.add(tripsNewForm.getDepartTimes().get(i));
			endTime.add(tripsNewForm.getArrivalTimes().get(i));
			departureName.add(tripsNewForm.getPlaceNames().get(i));
			arrivalName.add(tripsNewForm.getPlaceNames().get(i + 1));
			System.out.println("i=" + i + "の偶数行計算終わり");
			//奇数行
			if (i == arrivalTimesLnegth - 1) {
				break;
			}
			System.out.println("i=" + i + "の奇数行計算開始");
			rowSequence.add(i * 2 + 1);
			startTime.add(tripsNewForm.getArrivalTimes().get(i));
			endTime.add(tripsNewForm.getDepartTimes().get(i + 1));
			departureName.add("");
			arrivalName.add("");
			System.out.println("i=" + i + "の奇数行計算終わり");
		}
		ItineraryForm itineraryForm = new ItineraryForm();
		itineraryForm.setRowSequences(rowSequence);
		itineraryForm.setStartTimes(startTime);
		itineraryForm.setEndTimes(endTime);
		itineraryForm.setDepartureNames(departureName);
		itineraryForm.setArrivalNames(arrivalName);
		model.addAttribute("itineraryForm", itineraryForm);

		return "trips/itinerary";
	}

	//旅作成画面（旅程作成画面から戻る）
	@PostMapping("/new")
	public String newTripGoBack(ItineraryForm itineraryForm, Model model) {
		System.out.println("newTripに戻る");
		//出発時間、滞在時間、場所をitineraryFormからtripsNewFormに変換
		TripsNewForm tripsNewForm = new TripsNewForm();
		//出発時間
		List<LocalDateTime> departTimes = new ArrayList<>();
		departTimes.add(itineraryForm.getStartTimes().get(0));
		tripsNewForm.setDepartTimes(departTimes);
		//滞在時間、場所
		List<LocalTime> stayTimes = new ArrayList<>();
		List<String> placeNames = new ArrayList<>();
		int rowSequencesLength = itineraryForm.getRowSequences().size();
		for (int i = 0; i < rowSequencesLength; i++) {
			if (i % 2 == 0) { //偶数行(0スタート)
				placeNames.add(itineraryForm.getDepartureNames().get(i));
			}
			if (i == rowSequencesLength - 1) { //最終行のみ到着地を場所に追加
				placeNames.add(itineraryForm.getArrivalNames().get(i));
			}
			if (i % 2 == 1) {
				Duration stayTimeDuration = Duration.between(itineraryForm.getStartTimes().get(i),
						itineraryForm.getEndTimes().get(i));
				stayTimes.add(LocalTime.ofNanoOfDay(stayTimeDuration.toNanos()));
			}
		}
		tripsNewForm.setStayTimes(stayTimes);
		tripsNewForm.setPlaceNames(placeNames);
		System.out.println("旅程→旅作成戻る");
		model.addAttribute("tripsNewForm", tripsNewForm);
		return "trips/new";
	}

	//旅程確認登録画面
	@GetMapping("/confirm")
	public String newItineraryConfirm(Model model) {
		return "trips/confirm";
	}

}
