package com.koedia.activity.activityManager.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.koedia.activity.activityManager.model.entity.Activity;
import com.koedia.activity.activityManager.repository.ActivityRepository;
import com.koedia.activity.activityManager.service.ActivityService;

@Service("activityService")
public class ActivityServiceImpl implements ActivityService{

	@Autowired
	private ActivityRepository activityRepository;

	@Override
	public void saveActivity(Activity activity) {
		activityRepository.save(activity);
	}

	@Override
	public List<Activity> getAllActivities() {
		return activityRepository.findAll();
	}

	@Override
	public List<Activity> findAllByUserId(Integer userId) {
		return activityRepository.findAllByUserId(userId);
	}

	@Override
	public Activity findById(Integer id) {
		return activityRepository.findById(id);
	}
	
}
