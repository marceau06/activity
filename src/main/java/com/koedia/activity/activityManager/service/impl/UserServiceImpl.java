package com.koedia.activity.activityManager.service.impl;

import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.koedia.activity.activityManager.model.entity.Role;
import com.koedia.activity.activityManager.model.entity.User;
import com.koedia.activity.activityManager.repository.RoleRepository;
import com.koedia.activity.activityManager.repository.UserRepository;
import com.koedia.activity.activityManager.service.UserService;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private BCryptPasswordEncoder BCryptPasswordEncoder;

	@Override
	public User findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	@Override
	public User findUserByLastname(String lastname) {
		return userRepository.findByLastname(lastname);
	}
	
	@Override
	public User findUserById(Integer id) {
		return userRepository.findById(id);
	}
	
	@Override
	public void saveUser(User user) {
		user.setPassword(BCryptPasswordEncoder.encode(user.getPassword()));
		user.setActive(1);
		Role userRole = roleRepository.findByRole("USER");
		
		user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
		userRepository.save(user);
	}
}
