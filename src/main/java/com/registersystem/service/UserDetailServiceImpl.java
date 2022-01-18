package com.registersystem.service;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.registersystem.domain.Role;
import com.registersystem.domain.User;

@Service
public class UserDetailServiceImpl implements UserDetailsService {
	@Autowired
	UserService userService;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userService.findByUsername(username);
		Set<GrantedAuthority> ga = new HashSet<>();
		Role role = user.getRole();

		System.out.println("role.getRoleName()" + role.getName());
		ga.add(new SimpleGrantedAuthority(role.getName()));

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), ga);
	}

}