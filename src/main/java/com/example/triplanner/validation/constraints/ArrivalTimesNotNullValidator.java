package com.example.triplanner.validation.constraints;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class ArrivalTimesNotNullValidator implements ConstraintValidator<ArrivalTimesNotNull, List<LocalDateTime>>{
	
	@Override
	public void initialize(ArrivalTimesNotNull annotation) {
	}
	
	@Override
	public boolean isValid(List<LocalDateTime> arrivalTimes, ConstraintValidatorContext context) {
		if (arrivalTimes.size() == 0) {
			return false;
		} else if (arrivalTimes.get(0) == null) {
			return false;
		}
		return true;
	}

}
