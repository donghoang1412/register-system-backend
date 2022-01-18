package com.registersystem.jwt.models;

import java.util.Set;

import lombok.Getter;

@Getter
public class AuthenticationResponse {
	
	private final String jwt;
	private final String username;
	private final Set<String> roles;
	
	
	public AuthenticationResponse(String jwt, String username, Set<String>  roles) {
		super();
		this.jwt = jwt;
		this.username = username;
		this.roles = roles;
	}


}
