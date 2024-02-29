package com.example.triplanner.validation.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ReportAsSingleViolation;

@Documented
@Constraint(validatedBy = NotEmptyAtEvenValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
//@Repeatable(PlaceNamesLength.List.class)
@ReportAsSingleViolation
public @interface NotEmptyAtEven {

	String message() default "旅程のタイトルは全て入力してください";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
