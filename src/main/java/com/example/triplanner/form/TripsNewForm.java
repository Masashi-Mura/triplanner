package com.example.triplanner.form;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TripsNewForm {

	private LocalTime enteredDepartTimeValue;//tripsNewの出発時間の値保存用

	@Valid
	private List<@NotNull LocalDateTime> arrivalTimes;

	private List<LocalDateTime> departTimes;

	private List<String> placeNames;

	private List<LocalTime> stayTimes;

	//以下、itineraryページで取得
	private String tripTitle;
	
	private List<String> titles;
	
	private List<String> descriptions;

	private Integer publicId;

	private List<Integer> tagIds;
	
	private List<Double> latitudes;
	
	private List<Double> longitudes;
	
}
