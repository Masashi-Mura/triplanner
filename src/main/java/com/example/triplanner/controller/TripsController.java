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
import org.springframework.web.bind.annotation.RequestParam;

import com.example.triplanner.component.ReverseGeocoder;
import com.example.triplanner.entity.Itinerary;
import com.example.triplanner.entity.Prefecture;
import com.example.triplanner.entity.PublicOption;
import com.example.triplanner.entity.Purpose;
import com.example.triplanner.entity.Tag;
import com.example.triplanner.entity.TagLists;
import com.example.triplanner.entity.Trips;
import com.example.triplanner.form.ItineraryForm;
import com.example.triplanner.form.TripsNewForm;
import com.example.triplanner.repository.ItinerariesRepository;
import com.example.triplanner.repository.PrefectureRepository;
import com.example.triplanner.repository.PublicOptionRepository;
import com.example.triplanner.repository.PurposeRepository;
import com.example.triplanner.repository.TagListsRepository;
import com.example.triplanner.repository.TagRepository;
import com.example.triplanner.repository.TripsRepository;
import com.example.triplanner.repository.UsersRepository;

@Controller
@RequestMapping("/trips")
public class TripsController {

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private UsersRepository usersRepository;

	@Autowired
	private TripsRepository tripsRepository;

	@Autowired
	private TagListsRepository tagListsRepository;

	@Autowired
	private ItinerariesRepository itinerariesRepository;

	@Autowired
	private PrefectureRepository prefectureRepository;

	@Autowired
	private PublicOptionRepository publicOptionRepository;

	@Autowired
	private PurposeRepository purposeRepository;

	@Autowired
	private ReverseGeocoder reverceGeocoder;

	//旅一覧画面
	@GetMapping("/index")
	public String index(Model model) {

		//表示するtrip一覧を取得
		List<Trips> tripsList = tripsRepository.findAllByOrderById();
		model.addAttribute("tripsList", tripsList);

		//取得したtrip一覧のidに紐づく旅程を取得
		List<List<Itinerary>> itineraries = new ArrayList<>();
		tripsList.forEach(trip -> {
			List<Itinerary> oneTripItinerary = itinerariesRepository.findByTripIdOrderById(trip.getId());
			itineraries.add(oneTripItinerary);
		});
		model.addAttribute("itineraries", itineraries);

		//取得した旅程の場所名一覧を取得（indexのgoogleMapで経路指定に使用する形に変換)
		//(使用する形の例：&origin=東京駅&destination=大阪駅&waypoints=神奈川駅|名古屋駅)
		List<String> placeNamesQueries = new ArrayList<>();
		itineraries.forEach(itinerary -> {
			StringBuilder placeNamesQuery = new StringBuilder("&origin");
			placeNamesQuery.append("=" + itinerary.getFirst().getDepartureName());
			placeNamesQuery.append("&destination=" + itinerary.getLast().getArrivalName());
			placeNamesQuery.append("&waypoints=");
			//経由地（出発地と最終目的地を除いた場所）を連結
			placeNamesQuery.append(itinerary.get(2).getDepartureName());
			for (int i = 4; i < itinerary.size(); i++) {
				if (i % 2 == 0) {
					placeNamesQuery.append("|" + itinerary.get(i).getDepartureName());
				}
			}
			placeNamesQueries.add(placeNamesQuery.toString());
		});
		model.addAttribute("placeNamesQueries", placeNamesQueries);

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
		TripsNewForm tripsNewForm = new TripsNewForm();
		model.addAttribute("tripsNewForm", tripsNewForm);

		return "trips/new";
	}

	//旅作成画面（旅程作成画面から戻る）
	@PostMapping("/new")
	public String newTripGoBack(ItineraryForm itineraryForm, Model model) {
		System.out.println("@PostMapping(\"/new\") スタート");
		System.out.println("ItineraryFromは：" + itineraryForm);
		//出発時間、滞在時間、場所をitineraryFormからtripsNewFormに変換
		TripsNewForm tripsNewForm = new TripsNewForm();
		//滞在時間、場所を変換
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
		tripsNewForm.setDepartTimes(itineraryForm.getStartTimes());
		tripsNewForm.setTripTitle(itineraryForm.getTripTitle());
		tripsNewForm.setPublicId(itineraryForm.getPublicId());
		tripsNewForm.setTagIds(itineraryForm.getTagIds());
		model.addAttribute("tripsNewForm", tripsNewForm);

		System.out.println("@PostMapping(\"/new\") エンド");
		System.out.println("TripsNewFormは：" + tripsNewForm);

		return "trips/new";
	}

	//旅程作成画面
	@PostMapping("/itinerary")
	public String newItinerary(@Validated TripsNewForm tripsNewForm,
			BindingResult result, ItineraryForm itineraryForm,
			@RequestParam(value = "fromConfirm", defaultValue = "false") Boolean fromConfirm, Model model) {
		System.out.println();
		System.out.println("@PostMapping(\"/itinerary\") スタート");
		System.out.println("TripsNewFormは：" + tripsNewForm);
		System.out.println("ItineraryFromは：" + itineraryForm);
		// TripsNewFormのバリデーションエラー有
		// placeNamesが３個以上格納されていればOK
		// arrivalTimesに値が格納されていればOK
		if (result.hasErrors()) {
			System.out.println("@PostMapping(\"/itinerary\") result.hasError=True スタート");
			//出発地の出発時間をLocalDateからLocalDateTimeに変換しtripsNewFormに格納
			LocalDateTime enteredDepartTime = LocalDateTime.of(LocalDate.of(2020, 1, 1),
					tripsNewForm.getEnteredStartTimeValue());
			tripsNewForm.setDepartTimes(List.of(enteredDepartTime));
			model.addAttribute("tripsNewForm", tripsNewForm);
			System.out.println("@PostMapping(\"/itinerary\") result.hasError=True エンド");
			System.out.println("TripsNewFormは：" + tripsNewForm);
			return "trips/new";
		}

		// confirm画面から遷移。
		if (fromConfirm == true) {
			System.out.println("@PostMapping(\"/itinerary\") !getTripTitle().equals(\"\")=True スタート");
			//confirm画面のデータをmodelに追加
			model.addAttribute("itineraryForm", itineraryForm);
			/*itineraryFormの初期化 
			/*tagIdは1つ以上選択必須にしてるためTagIdsがnullの時は存在しないが、万が一nullの場合
			/*viewがエラー表示になるため初期化している。*/
			if (itineraryForm.getTagIds() == null) {
				itineraryForm.setTagIds(new ArrayList<>());
			}
			//マスタ(tag,公開設定、目的)をmodelに追加
			List<Tag> tags = tagRepository.findAllByOrderById();
			model.addAttribute("tags", tags);
			List<PublicOption> publicOptions = publicOptionRepository.findAllByOrderById();
			model.addAttribute("publicOptions", publicOptions);
			List<Purpose> purposes = purposeRepository.findAllByOrderById();
			model.addAttribute("purposes", purposes);

			System.out.println("@PostMapping(\"/itinerary\") !getTripTitle().equals(\"\")=True エンド");
			System.out.println("ItineraryFromは：" + itineraryForm);

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
		int arrivalTimesLength = tripsNewForm.getArrivalTimes().size();
		System.out.println("到着時間長" + arrivalTimesLength);
		System.out.println("tripsNewFormの中身=" + tripsNewForm);
		for (int i = 0; i < arrivalTimesLength; i++) {
			//偶数行
			System.out.println("偶数行開始 i=" + i);
			rowSequence.add(i * 2);
			purposeIds.add(1);
			startTime.add(tripsNewForm.getDepartTimes().get(i));
			endTime.add(tripsNewForm.getArrivalTimes().get(i));
			departurePrefectureIds.add(reverceGeocoder.getPrefectureId(
					tripsNewForm.getLatitudes().get(i), tripsNewForm.getLongitudes().get(i)));
			departureName.add(tripsNewForm.getPlaceNames().get(i));
			arrivalName.add(tripsNewForm.getPlaceNames().get(i + 1));
			System.out.println("偶数行終了 i=" + i);
			//奇数行
			System.out.println("奇数行開始 i=" + i);
			if (i == arrivalTimesLength - 1) {
				break;
			}
			rowSequence.add(i * 2 + 1);
			purposeIds.add(2);
			startTime.add(tripsNewForm.getArrivalTimes().get(i));
			endTime.add(tripsNewForm.getDepartTimes().get(i + 1));
			departurePrefectureIds.add(null);
			departureName.add("");
			arrivalName.add("");
			System.out.println("奇数行終了 i=" + i);
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

		System.out.println("@PostMapping(\"/itinerary\") エンド");
		System.out.println("TripsNewFormは：" + tripsNewForm);
		System.out.println("ItineraryFromは：" + itineraryForm);

		return "trips/itinerary";
	}

	//旅程確認画面
	@PostMapping("/confirm")
	public String newItineraryConfirm(@Validated ItineraryForm itineraryForm, BindingResult result, Model model) {
		System.out.println("@PostMapping(\"/confirm\") スタート");
		System.out.println("ItineraryFromは：" + itineraryForm);
		if (result.hasErrors()) {
			System.out.println("@PostMapping(\"/confirm\") result.hasErrors=true スタート");
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
			//purposeIdのバリデーションエラーメッセージ
			model.addAttribute("purposeIdsMessage", "行動のプルダウンを全て選択してください");

			System.out.println("@PostMapping(\"/confirm\") result.hasErrors=true エンド");
			System.out.println("ItineraryFromは：" + itineraryForm);

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
			if (description != null && !description.isEmpty()) {
				usedBrDescriptions.add(description.replaceAll("\r\n", "<br>"));
			} else {
				usedBrDescriptions.add("");
			}
		});
		itineraryForm.setUsedBrDescriptions(usedBrDescriptions);

		System.out.println("@PostMapping(\"/confirm\") エンド");
		System.out.println("ItineraryFromは：" + itineraryForm);

		model.addAttribute("itineraryForm", itineraryForm);
		return "trips/confirm";
	}

	//旅程登録画面
	@PostMapping("/post")
	public String tripPost(ItineraryForm itineraryForm, Model model) {

		//tripsをDB(tripsテーブル)に保存
		Trips trip = new Trips();
		//userIdを取得してtripに設定
		//ここに、ログインアカウントのuserIdを取得するコード記載
		trip.setUserId(1);
		trip.setTripTitle(itineraryForm.getTripTitle());
		trip.setTotalTripDays(itineraryForm.getEndTimes().get(itineraryForm.getEndTimes().size() - 1).getDayOfMonth());
		trip.setPublicId(itineraryForm.getPublicId());
		trip.setDeleted(false);
		tripsRepository.save(trip);

		//tagIdをDB(tag_listsテーブル)に保存
		itineraryForm.getTagIds().forEach(tagId -> {
			TagLists tagList = new TagLists();
			tagList.setTagId(tagId);
			tagList.setTripId(trip.getId());
			tagListsRepository.save(tagList);
		});

		//itineraryをDB(itinerariesテーブル)に保存
		int rowNumber = itineraryForm.getRowSequences().size();
		for (int i = 0; i < rowNumber; i++) {
			Itinerary itinerary = new Itinerary();
			itinerary.setTripId(trip.getId());
			itinerary.setRowSequence(itineraryForm.getRowSequences().get(i));
			itinerary.setPurposeId(itineraryForm.getPurposeIds().get(i));
			itinerary.setStartTime(itineraryForm.getStartTimes().get(i));
			itinerary.setEndTime(itineraryForm.getEndTimes().get(i));
			itinerary.setDepartureName(itineraryForm.getDepartureNames().get(i));
			itinerary.setDeparturePrefectureId(itineraryForm.getDeparturePrefectureIds().get(i));
			itinerary.setArrivalName(itineraryForm.getArrivalNames().get(i));
			itinerary.setTitle(itineraryForm.getTitles().get(i));
			itinerary.setDescription(itineraryForm.getDescriptions().get(i));
			itinerariesRepository.save(itinerary);
		}

		return "redirect:/trips/index";
	}

}
