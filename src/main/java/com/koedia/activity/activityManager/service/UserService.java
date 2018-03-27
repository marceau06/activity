package com.koedia.activity.activityManager.service;

import com.koedia.activity.activityManager.model.entity.User;

public interface UserService {
	
	public User findUserByEmail(String email);
	
	public User findUserByLastname(String lastname);
	
	public 	User findUserById(Integer id);

	public void saveUser(User user);

}
