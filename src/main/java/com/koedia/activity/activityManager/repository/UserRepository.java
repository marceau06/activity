package com.koedia.activity.activityManager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.koedia.activity.activityManager.model.entity.User;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Integer>{

	User findByEmail(String email);
	
	User findByLastname(String lastname);
	
	User findById(Integer id);
	
}

