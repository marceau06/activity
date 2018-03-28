package com.koedia.activity.activityManager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.koedia.activity.activityManager.model.entity.Activity;

@Repository("activityRepository")
public interface ActivityRepository extends JpaRepository<Activity, Integer>{
	
	List<Activity> findAllByUserId(Integer userId);
	
	Activity findById(Integer id);
	
	void deleteById(Integer id);
}
