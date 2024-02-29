package com.example.triplanner.validation.constraints;

import java.util.List;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NotEmptyAtEvenValidator implements ConstraintValidator<NotEmptyAtEven, List<String>> {

	@Override
	public void initialize(NotEmptyAtEven annotation) {
	}

	@Override
	public boolean isValid(List<String> titles, ConstraintValidatorContext context) {
		for (int i = 1; i < titles.size(); i = i + 2) {
			String title = titles.get(i);
			if (title != null && !title.equals("")) {
				return true;
			} 
		}
		return false;
	}

}
