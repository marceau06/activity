package com.koedia.activity.activityManager.validation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.Payload;

@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EndDateAfterBeginDateValidator.class)
public @interface EndDateAfterBeginDate {

	String message() default "The end hour should be after the starting hour";

	Class<?>[] groups() default {};	
	
	Class<? extends Payload>[] payload() default {};
	
}
