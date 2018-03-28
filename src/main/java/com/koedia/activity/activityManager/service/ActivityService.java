package com.koedia.activity.activityManager.service;

import java.util.List;

import com.koedia.activity.activityManager.model.entity.Activity;

public interface ActivityService {
	
	public void saveActivity(Activity activity);
	
	public List<Activity> getAllActivities();
	
	public List<Activity> findAllByUserId(Integer userId);
	
	public Activity findById(Integer id);
	
	public void deleteById(Integer id);
	
}
