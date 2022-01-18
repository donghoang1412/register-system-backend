package com.registersystem.service;


import java.util.List;

import com.registersystem.domain.Role;
import com.registersystem.domain.User;


public interface UserService {
	public List<User> findAll();
	public User save(User u);
	public void deleteUserById(int uId);
	public User findByUserId(int uId);
	public User findByUsername(String username);
	public List<User> findByUsernameContains(String username);
	public List<User> findByFirstNameContains(String firstName);
	public List<User> findByCourses(int courseId);
	
}