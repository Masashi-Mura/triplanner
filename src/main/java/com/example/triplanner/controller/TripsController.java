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

import com.example.triplanner.component.ReverceGeocoder;
import com.example.triplanner.entity.Prefecture;
import com.example.triplanner.entity.PublicOption;
import com.example.triplanner.entity.Purpose;
import com.example.triplanner.entity.Tag;
import com.example.triplanner.form.ItineraryForm;
import com.example.triplanner.form.TripsNewForm;
import com.example.triplanner.repository.PrefectureRepository;
import com.example.triplanner.repository.PublicOptionRepository;
import com.example.triplanner.repository.PurposeRepository;
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

	@Autowired
	private PublicOptionRepository publicOptionRepository;

	@Autowired
	private PurposeRepository purposeRepository;

	@Autowired
	private ReverceGeocoder reverceGeocoder;

	//旅一覧画面
	@GetMapping("/index")
	public String index(Model model) {
		List<Tag> tags = tagRepository.findAllByOrderById();
		model.addAttribute("tags", tags);

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
		List<Integer> tagIds = new ArrayList<>();
		tripsNewForm.setTagIds(tagIds);
		model.addAttribute("tripsNewForm", tripsNewForm);

		return "trips/new";
	}

	//旅程作成画面
	@PostMapping("/itinerary")
	public String newItinerary(@Validated TripsNewForm tripsNewForm,
			BindingResult result, ItineraryForm itineraryForm, Model model) {
		System.out.println("旅程作成画面コントローラ開始");
		// TripsNewFormのバリデーションエラー有
		if (result.hasErrors()) {
			//出発時刻をLocalDateからLocalDateTimeに変換しmodelに追加
			LocalDateTime enteredDepartTime = LocalDateTime.of(LocalDate.of(2020, 1, 1),
					tripsNewForm.getEnteredDepartTimeValue());
			tripsNewForm.setDepartTimes(List.of(enteredDepartTime));
			model.addAttribute("tripsNewForm", tripsNewForm);
			//モデルにエラーの設定
			model.addAttribute("hasMessage", true);
			model.addAttribute("message", "ルート検索を行い時間を設定してください。");

			return "trips/new";
		}

		// confirm画面から遷移。
		if (!itineraryForm.getTripTitle().equals("")) {
			//confirm画面のデータをmodelに追加
			model.addAttribute("itineraryForm", itineraryForm);
			//itineraryFormの初期化
			if (itineraryForm.getTagIds() == null) {
				itineraryForm.setTagIds(new ArrayList<>());
			}
			//tagマスタ、公開設定マスタ、目的マスタをmodelに追加
			List<Tag> tags = tagRepository.findAllByOrderById();
			model.addAttribute("tags", tags);
			List<PublicOption> publicOptions = publicOptionRepository.findAllByOrderById();
			model.addAttribute("publicOptions", publicOptions);
			List<Purpose> purposes = purposeRepository.findAllByOrderById();
			model.addAttribute("purposes", purposes);

			return "trips/itinerary";
		}

		// tripsNewFormをitineraryFormに変換しmodelに追加
		List<Integer> rowSequence = new ArrayList<>();
		List<Integer> purposeIds = new ArrayList<>();
		List<LocalDateTime> startTime = new ArrayList<>();
		List<LocalDateTime> endTime = new ArrayList<>();
		List<Integer> departurePrefectureIds = new ArrayList<>();
		List<String> departureName = new ArrayList<>();
		List<String> arrivalName = new ArrayList<>();
		List<String> titles = new ArrayList<>();
		List<String> descriptions = new ArrayList<>();
		int arrivalTimesLnegth = tripsNewForm.getArrivalTimes().size();
		for (int i = 0; i < arrivalTimesLnegth; i++) {
			//偶数行
			rowSequence.add(i * 2);
			purposeIds.add(1);
			startTime.add(tripsNewForm.getDepartTimes().get(i));
			endTime.add(tripsNewForm.getArrivalTimes().get(i));
			departurePrefectureIds.add(reverceGeocoder.getPrefectureId(
					tripsNewForm.getLatitudes().get(i), tripsNewForm.getLongitudes().get(i)));
			departureName.add(tripsNewForm.getPlaceNames().get(i));
			arrivalName.add(tripsNewForm.getPlaceNames().get(i + 1));
			//奇数行
			if (i == arrivalTimesLnegth - 1) {
				break;
			}
			rowSequence.add(i * 2 + 1);
			purposeIds.add(0);
			startTime.add(tripsNewForm.getArrivalTimes().get(i));
			endTime.add(tripsNewForm.getDepartTimes().get(i + 1));
			departurePrefectureIds.add(null);
			departureName.add("");
			arrivalName.add("");
		}

		if (tripsNewForm.getTitles() == null) {
			for (int i = 0; i < rowSequence.size(); i++) {
				titles.add("");
			}
		}
		if (tripsNewForm.getDescriptions() == null) {
			for (int i = 0; i < rowSequence.size(); i++) {
				descriptions.add("");
			}
		}

		itineraryForm.setRowSequences(rowSequence);
		itineraryForm.setPurposeIds(purposeIds);
		itineraryForm.setStartTimes(startTime);
		itineraryForm.setEndTimes(endTime);
		itineraryForm.setDeparturePrefectureIds(departurePrefectureIds);
		itineraryForm.setDepartureNames(departureName);
		itineraryForm.setArrivalNames(arrivalName);
		itineraryForm.setTitles(titles);
		itineraryForm.setDescriptions(descriptions);
		itineraryForm.setTripTitle(tripsNewForm.getTripTitle());
		itineraryForm.setPublicId(tripsNewForm.getPublicId());
		if (tripsNewForm.getTagIds() == null) {
			itineraryForm.setTagIds(new ArrayList<>());
		} else {
			itineraryForm.setTagIds(tripsNewForm.getTagIds());
		}
		model.addAttribute("itineraryForm", itineraryForm);

		//tagマスタ、公開設定マスタ、目的マスタをmodelに追加
		List<Tag> tags = tagRepository.findAllByOrderById();
		model.addAttribute("tags", tags);
		List<PublicOption> publicOptions = publicOptionRepository.findAllByOrderById();
		model.addAttribute("publicOptions", publicOptions);
		List<Purpose> purposes = purposeRepository.findAllByOrderById();
		model.addAttribute("purposes", purposes);

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
		tripsNewForm.setTripTitle(itineraryForm.getTripTitle());
		tripsNewForm.setPublicId(itineraryForm.getPublicId());
		tripsNewForm.setTagIds(itineraryForm.getTagIds());
		System.out.println("旅程→旅作成戻る");
		model.addAttribute("tripsNewForm", tripsNewForm);

		return "trips/new";
	}

	//旅程確認画面
	@PostMapping("/confirm")
	public String newItineraryConfirm(@Validated ItineraryForm itineraryForm, BindingResult result, Model model) {
		if (result.hasErrors()) {
			//itineraryFormを設定
			if (itineraryForm.getTagIds() == null) {
				itineraryForm.setTagIds(new ArrayList<>());
			}
			model.addAttribute("itineraryForm", itineraryForm);
			//tagマスタ、公開設定マスタ、目的マスタをmodelに追加
			List<Tag> tags = tagRepository.findAllByOrderById();
			model.addAttribute("tags", tags);
			List<PublicOption> publicOptions = publicOptionRepository.findAllByOrderById();
			model.addAttribute("publicOptions", publicOptions);
			List<Purpose> purposes = purposeRepository.findAllByOrderById();
			model.addAttribute("purposes", purposes);

			return "trips/itinerary";
		}

		//ユーザが選択したpurposeIdを文字に変換
		List<Integer> selectedPurposeIds = itineraryForm.getPurposeIds();
		List<String> purposeStrings = new ArrayList<>();
		List<Purpose> purposes = purposeRepository.findAllByOrderById();
		selectedPurposeIds.forEach(id -> {
			purposeStrings.add(purposes.get(id - 1).getPurpose());
		});
		itineraryForm.setPurposeStrings(purposeStrings);

		//ユーザが選択した投稿設定を文字に変換
		int publicId = itineraryForm.getPublicId();
		List<PublicOption> publicOptions = publicOptionRepository.findAllByOrderById();
		itineraryForm.setPublicString(publicOptions.get(publicId - 1).getName());

		//ユーザが選択したtagIdを文字に変換
		List<Integer> selectedTagIds = itineraryForm.getTagIds();
		List<String> tagStrings = new ArrayList<>();
		List<Tag> tags = tagRepository.findAllByOrderById();
		selectedTagIds.forEach(id -> {
			tagStrings.add(tags.get(id - 1).getName());
		});
		itineraryForm.setTagStrings(tagStrings);
		
		//textareaの改行を<br>に変換
		List<String> usedBrDescriptions = new ArrayList<>();
		itineraryForm.getDescriptions().forEach(description -> {
			if(description != null && !description.isEmpty()) {
				usedBrDescriptions.add(description.replaceAll("\r\n", "<br>"));
			} else {
				usedBrDescriptions.add("");
			}
		});
		itineraryForm.setUsedBrDescriptions(usedBrDescriptions);
		

		model.addAttribute("itineraryForm", itineraryForm);
		return "trips/confirm";
	}

}
