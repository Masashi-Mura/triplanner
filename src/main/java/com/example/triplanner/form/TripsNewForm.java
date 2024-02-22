package com.example.triplanner.form;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.example.triplanner.validation.constraints.ArrivalTimesNotNull;
import com.example.triplanner.validation.constraints.PlaceNamesLength;

import lombok.Data;

@Data
public class TripsNewForm {

	private LocalTime enteredStartTimeValue;//tripsNewの出発時間の値保存用

	@ArrivalTimesNotNull
	private List<LocalDateTime> arrivalTimes;

	private List<LocalDateTime> departTimes;

	@PlaceNamesLength(min = 3)	//場所が3カ所以上入力されているか確認
	private List<String> placeNames;

	private List<Double> latitudes;

	private List<Double> longitudes;

	//tripsNew画面のみ使用。itineraryから戻るときはコントローラで計算して格納
	private List<LocalTime> stayTimes;

	//以下、itineraryページで取得
	private String tripTitle;

	private List<String> titles;

	private List<String> descriptions;

	private Integer publicId;

	private List<Integer> tagIds;

}
