package com.example.triplanner.form;

import java.util.Date;

import lombok.Data;


@Data
public class ItineraryForm {

//	private Integer id;
	
//	private Integer tripId;
	
	private Integer rowSequence;

	private Integer purposeId;
	
	private Date startTime;
	
	private Date endTime;
	
	private String departureName;
	
	private Integer departurePrefectureId;
	
	private String arrivalName;
	
	private String title;
	
	private String description;
}
