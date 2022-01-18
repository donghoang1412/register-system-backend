package com.registersystem;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.registersystem.repository.CourseRepository;
import com.registersystem.repository.UserRepository;
import com.registersystem.service.CourseService;
import com.registersystem.service.UserService;

@SpringBootTest
class RegisterSystemApplicationTests {

	@Autowired
	private CourseService courseService;
	
	@Autowired
	private UserService userService;
	
	@Mock
	private CourseRepository courseRepo;
	
	@Mock
	private UserRepository userRepo;
	

	
	@Test
	void getCoursesTest() {
		assertEquals(1, courseService.getCourse().size());
	}
	
	@Test
	void getUsersTest() {
		assertEquals(1, userService.findByUsernameContains("dong").size());
		assertEquals(4, userService.findAll().size());
	}

}
