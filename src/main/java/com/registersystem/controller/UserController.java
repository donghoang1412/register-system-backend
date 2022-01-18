package com.registersystem.controller;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.registersystem.domain.User;
import com.registersystem.jwt.JwtUtil;
import com.registersystem.jwt.models.AuthenticationRequest;
import com.registersystem.jwt.models.AuthenticationResponse;
import com.registersystem.service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {

	@Autowired
	UserService userService;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private JwtUtil jwtTokenUtil;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	private final String admin = "ROLE_ADMIN";
	private final String professor = "ROLE_PROFESSOR";
	private final String student = "ROLE_STUDENT";

	@PostMapping(value = "/saveUser")
	public ResponseEntity<?> saveUser(@RequestBody String body) {
		Map<String, Object> map = new HashMap<>();
		Gson gson = new Gson();
		User user = gson.fromJson(body, User.class);
		String username = user.getUsername();
		User savedUser = userService.findByUsername(username);
		if (savedUser == null) {	
			String encodedPassword = passwordEncoder.encode(user.getPassword());
			user.setPassword(encodedPassword);
			User persistUser = userService.save(user);
			map.put("user", persistUser);
			map.put("status", "successful");
		} else {
			map.put("status", "username's already exist");
		}
		return ResponseEntity.ok(map);
	}

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
			throws Exception {
		try {
			System.out.println(authenticationRequest.getPassword());
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					authenticationRequest.getUsername(), authenticationRequest.getPassword()));
		} catch (BadCredentialsException e) {
			throw new Exception("Incorrect username or password", e);
		}

		final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
		final String jwt = jwtTokenUtil.generateToken(userDetails);
		Set<String> roles = new HashSet<>();
		userDetails.getAuthorities().forEach(e -> roles.add(e.toString()));
		AuthenticationResponse authenticationJwt = new AuthenticationResponse(jwt, userDetails.getUsername(), roles);
		return ResponseEntity.ok(authenticationJwt);
	}

	@GetMapping(value = "/getUserFirstNameAndLastName")
	public ResponseEntity<?> getUserFirstNameAndLastName(@RequestParam(value = "username") String username) {
		Map<String, String> map = new HashMap<>();
		User savedUser = userService.findByUsername(username);
		map.put("firstName", savedUser.getFirstName());
		map.put("lastName", savedUser.getLastName());
		return ResponseEntity.ok(map);
	}

	@GetMapping(value = "/getUserByUsernameContains")
	public ResponseEntity<?> getUserByUsernameContains(@RequestParam(value = "username") String username) {
		List<User> savedUsersList = userService.findByUsernameContains(username);
		savedUsersList.removeIf(user -> user.getRole().getName().equals("ROLE_ADMIN"));
		savedUsersList.removeIf(user -> user.getRole().getName().equals("ROLE_PROFESSOR"));

		return ResponseEntity.ok(savedUsersList);
	}

	@GetMapping(value = "/getAllStudents")
	public ResponseEntity<?> getAllStudents() {
		List<User> savedUsersList = userService.findAll();
		savedUsersList.removeIf(user -> user.getRole().getName().equals(admin));
		savedUsersList.removeIf(user -> user.getRole().getName().equals(professor));
		return ResponseEntity.ok(savedUsersList);
	}

	@GetMapping(value = "/getAllProfessor")
	public ResponseEntity<?> getAllProfessor() {
		List<User> savedUsersList = userService.findAll();
		savedUsersList.removeIf(user -> user.getRole().getName().equals(admin));
		savedUsersList.removeIf(user -> user.getRole().getName().equals(student));
		return ResponseEntity.ok(savedUsersList);
	}

	@GetMapping(value = "/getProfessorByFirstNameContains")
	public ResponseEntity<?> getProfessorByFirstNameContains(@RequestParam(value = "firstName") String firstName) {
		List<User> savedUsersList = userService.findByFirstNameContains(firstName);
		savedUsersList.removeIf(user -> user.getRole().getName().equals(admin));
		savedUsersList.removeIf(user -> user.getRole().getName().equals(student));
		return ResponseEntity.ok(savedUsersList);
	}

	@PostMapping(value = "/editUser")
	public ResponseEntity<?> editUser(@RequestBody String body) {
		Gson gson = new Gson();
		User editUser = gson.fromJson(body, User.class);
		int id = editUser.getId();
		User savedUser = userService.findByUserId(id);
		savedUser.setFirstName(editUser.getFirstName());
		savedUser.setLastName(editUser.getLastName());
		User persistUser = userService.save(savedUser);
		return ResponseEntity.ok(persistUser);
	}
	
	/*
	@GetMapping(value="/getStudentList")
	public ResponseEntity<?> getStudentList(@RequestParam(value="courseId") int courseId) {
		List<User> users = userService.findByCourses(courseId);
		users.removeIf(user -> user.getRole().getName().equals(professor));
		Map<String, String> studentList = new HashMap<>();
		for(User user : users) {
			String studentName = user.getFirstName()+ " " + user.getLastName();
			studentList.put("name", studentName);
		}
		return ResponseEntity.ok(studentList);
	} */
}
