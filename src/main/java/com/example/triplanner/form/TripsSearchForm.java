package com.example.triplanner.form;

import java.util.List;

import lombok.Data;

@Data
public class TripsSearchForm {

	private Integer days;

	private Integer spoint;
	
	private List<Integer> waypoints;

	private List<Integer> tags;

}
