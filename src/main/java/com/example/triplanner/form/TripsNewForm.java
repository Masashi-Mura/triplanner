package com.example.triplanner.form;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TripsNewForm {

	@NotNull
	private List<LocalDateTime> arrivalTimes;

	private List<LocalDateTime> departTimes;

	private List<String> placeNames;

	private List<LocalTime> stayTimes;

}
