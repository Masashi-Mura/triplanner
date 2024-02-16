package com.example.triplanner.form;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class ItineraryForm {

	//	private Integer id;

	//	private Integer tripId;
	
	//以下、itineraryテーブル

	private List<Integer> rowSequences;

	private List<Integer> purposeIds;

	private List<LocalDateTime> startTimes;

	private List<LocalDateTime> endTimes;

	private List<String> departureNames;

	private List<Integer> departurePrefectureIds;

	private List<String> arrivalNames;

	private List<String> titles;

	private List<String> descriptions;
	
	//以下、itineraryで取得
	private String tripTitle;
	
	private Integer publicId;
	
	private List<Integer> tagIds;
	
}
