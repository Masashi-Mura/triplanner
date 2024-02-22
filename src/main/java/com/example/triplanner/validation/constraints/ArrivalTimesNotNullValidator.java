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
		if (arrivalTimes == null) {
			return true;//確認画面から戻る時にtrue。場所が1カ所のみもtrueだが場所の個数のValidateでfalseになる。
		} else if (arrivalTimes.size() == 0) {
			return false;
		} else if (arrivalTimes.get(0) != null) {
			return true;
		}
		return false;
	}

}
