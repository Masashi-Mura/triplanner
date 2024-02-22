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
@Constraint(validatedBy = ArrivalTimesNotNullValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
//@Repeatable(PlaceNamesLength.List.class)
@ReportAsSingleViolation
public @interface ArrivalTimesNotNull {

	String message() default "ルート検索を行い時間を設定してください。";

	Class<?>[] groups() default {};

	Class<? extends Payload>[] payload() default {};

}
