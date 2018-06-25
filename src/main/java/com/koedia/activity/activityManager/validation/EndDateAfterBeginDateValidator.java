package com.koedia.activity.activityManager.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.koedia.activity.activityManager.model.entity.Activity;

public class EndDateAfterBeginDateValidator implements ConstraintValidator<EndDateAfterBeginDate, Activity> {

	@Override
	public void initialize(EndDateAfterBeginDate constraintAnnotation) {
	}

	@Override
	public boolean isValid(Activity activity, ConstraintValidatorContext context) {
		if (activity == null) {
			return false;
		}
		
		return activity.getBeginDate().compareTo(activity.getEndDate()) > 0;
	}
	
}
