package com.example.triplanner.validation.constraints;

import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PlaceNamesLengthValidator implements ConstraintValidator<PlaceNamesLength, List<String>>{
	
	int min;
	
	@Override
	public void initialize(PlaceNamesLength annotation) {
		this.min = annotation.min();
	}
	
	@Override
	public boolean isValid(List<String> placeNames, ConstraintValidatorContext context) {
		if (placeNames.size() < this.min) {
			return false;
		}
		return true;
	}

}
