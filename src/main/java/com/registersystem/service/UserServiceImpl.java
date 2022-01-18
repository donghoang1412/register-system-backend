package com.registersystem.service;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.registersystem.domain.Role;
import com.registersystem.domain.User;
import com.registersystem.repository.UserRepository;


@Service
public class UserServiceImpl implements UserService{

	@Autowired
	UserRepository userRepository;

	@Override
	public List<User> findAll() {
		return userRepository.findAll();
	}

	@Override
	public User save(User u) {
		return userRepository.save(u);
	}

	@Override
	public void deleteUserById(int uId) {
		userRepository.deleteById(uId);
	}

	@Override
	public User findByUserId(int uId) {
		Optional<User> u = userRepository.findById(uId);
		if(u.isPresent())
			return u.get();
		else
			return null;
	}

	@Override
	public User findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public List<User> findByUsernameContains(String username) {
		return userRepository.findByUsernameContains(username);
	}

	@Override
	public List<User> findByFirstNameContains(String firstName) {
		return userRepository.findByFirstNameContains(firstName);
	}

	@Override
	public List<User> findByCourses(int courseId) {
		return userRepository.findUserByCourses(courseId);
	}


	

}

