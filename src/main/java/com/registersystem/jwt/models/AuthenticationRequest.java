package com.registersystem.jwt.models;

import com.registersystem.domain.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationRequest {
	private String username;
	private String password;
	private Role role;
	
	
	
	public AuthenticationRequest(String username, String password, Role role) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public AuthenticationRequest() {
		super();
	}

	
	
}
