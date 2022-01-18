package com.registersystem.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;
import com.registersystem.domain.Course;
import com.registersystem.domain.User;
import com.registersystem.dto.CourseDisplay;
import com.registersystem.dto.CourseProfessorDisplay;
import com.registersystem.repository.UserRepository;
import com.registersystem.service.CourseService;
import com.registersystem.service.UserService;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class CourseController {
	
	@Autowired
	CourseService courseService;
	
	@Autowired
	UserService userService;
	
	private final String admin = "ROLE_ADMIN";
	private final String professor = "ROLE_PROFESSOR";
	private final String student = "ROLE_STUDENT";
	
	/* This is the post method to save the course object to the database*/
	@PostMapping(value = "/saveCourse")
	public ResponseEntity<?> saveCourse (@RequestBody String body, @RequestParam(value="id") int id) {
		System.out.println(body);
		Map<String, Object> map = new HashMap<>();
		Gson gson = new Gson();
		Course course = gson.fromJson(body, Course.class);
		User user = userService.findByUserId(id);
		Set<Course> courses = user.getCourses();
		if(courses == null) {
			courses = new HashSet<>();
		}
		for(Course savedCourse : courses) {
			if(savedCourse.getCourseName().equals(course.getCourseName())) {
				map.put("status", "This course is already taught by this professor!");
				return ResponseEntity.ok(map);
			}
		}
		courses.add(course);
		User persistUser = userService.save(user);
		map.put("status", "successful");
		map.put("user", persistUser);
		return ResponseEntity.ok(map);
	}
	
	/* This is a get method to get all the courses */
	@GetMapping(value= "/getCourses")
	public ResponseEntity<?> getCourses() {
		List<User> profList = userService.findAll();
		profList.removeIf(user -> user.getRole().getName().equals(admin));
		profList.removeIf(user -> user.getRole().getName().equals(student));
		List<CourseDisplay> courseList = new ArrayList<>();
		for(User eachProf : profList) {
			Set<Course> courses = eachProf.getCourses();
			for(Course eachCourse : courses) {
				CourseDisplay courseDP = new CourseDisplay();
				courseDP.setCourseName(eachCourse.getCourseName());
				courseDP.setCapacity(eachCourse.getCapacity());
				courseDP.setDescription(eachCourse.getDescription());
				courseDP.setId(eachCourse.getId());
				courseDP.setProfName(eachProf.getFirstName()+" " + eachProf.getLastName());
				courseList.add(courseDP);
			}
		}
		return ResponseEntity.ok(courseList);
	}
	
	/* This is a get method to get the courses that contains the name users want to search */
	/*
	@GetMapping(value= "/getCourse")
	public List<Course> getCourse(@RequestParam(value = "courseName") String courseName) {
		List<Course> courses = courseService.getCoursesByNameContains(courseName);
		return courses;
	} */
	
	/* This is a get method to get the courses a professor teaches */
	@GetMapping(value= "/getCoursesByProfessor")
	public Set<Course> getCourseByProfessor(@RequestParam(value = "id") int id) {
		User savedUser = userService.findByUserId(id);
		return savedUser.getCourses();
	}
	
	/* This is a get method to get the courses a student enrolls */
	@GetMapping(value= "/getCoursesEnrolled")
	public ResponseEntity<?> getCoursesEnrolled(@RequestParam(value = "username") String username) {
		User savedUser = userService.findByUsername(username);
		Set<Course> courses = savedUser.getCourses();
		System.out.println("courses: " + courses);
		List<CourseDisplay> courseList = new ArrayList<>();
		List<User> prof = new ArrayList<>();	
		for(Course course : courses) {
			prof = userService.findByCourses(course.getId());
			prof.removeIf(user -> user.getRole().getName().equals(student));
			CourseDisplay courseDP = new CourseDisplay();
			courseDP.setCourseName(course.getCourseName());
			courseDP.setCapacity(course.getCapacity());
			courseDP.setDescription(course.getDescription());
			courseDP.setId(course.getId());
			courseDP.setProfName(prof.get(0).getFirstName()+" " + prof.get(0).getLastName());
			courseList.add(courseDP);
		}
		return ResponseEntity.ok(courseList);
	}

	/* This is a get method to update the course */
	@PostMapping(value= "/updateCourse")
	public ResponseEntity<?> updateCourse(@RequestBody String body) {
		System.out.println(body);
		Map<String, String> map = new HashMap<>();
		Gson gson = new Gson();
		Course newCourse = gson.fromJson(body, Course.class);
		int id = newCourse.getId();
		Course savedCourse = courseService.getCourseById(id);
		savedCourse = newCourse;
		courseService.saveCourse(savedCourse);
		map.put("status", "successful");
		return ResponseEntity.ok(map);
	}
	
	@GetMapping(value ="/deleteCourse")
	public ResponseEntity<?> deleteCourse(@RequestParam(value="profId") int professorId,@RequestParam(value="courseId") int courseId){
		Map<String, Object> map = new HashMap<>();
		User savedUser = userService.findByUserId(professorId);
		Set<Course> savedCourses = savedUser.getCourses();
		savedCourses.removeIf(course -> course.getId() == courseId);
		User persistUser = userService.save(savedUser);
		courseService.deleteById(courseId);
		map.put("user", persistUser);
		return ResponseEntity.ok(map);
	}
	
	@GetMapping(value ="/enrollCourse")
	public ResponseEntity<?> enrollCourse(@RequestParam(value="courseId") int courseId, @RequestParam(value="username") String username){
		Map<String, Object> map = new HashMap<>();
		User savedUser = userService.findByUsername(username);
		Course course = courseService.getCourseById(courseId);
		Set<Course> courses = savedUser.getCourses();
		if(courses.contains(course)) {
			map.put("status", "Fail");
			return ResponseEntity.ok(map);
		} 
		courses.add(course);
		User persistUser = userService.save(savedUser);
		map.put("status", "Success");
		map.put("user", persistUser);
		return ResponseEntity.ok(map);
	}	
	
	@GetMapping(value ="/getCoursesProfessorDisplay")
	public ResponseEntity<?> getCoursesProfessorDisplay(@RequestParam(value="username") String username){
		User savedUser = userService.findByUsername(username);
		Set<Course> courses = savedUser.getCourses();
		List<CourseProfessorDisplay> courseList = new ArrayList<>();
		for(Course course: courses) {
			CourseProfessorDisplay cpd = new CourseProfessorDisplay();
			cpd.setId(course.getId());
			cpd.setCourseName(course.getCourseName());
			cpd.setCapacity(course.getCapacity());
			cpd.setDescription(course.getDescription());
			List<User> users = userService.findByCourses(course.getId());
			users.removeIf(user -> user.getRole().getName().equals(professor));
			List<String> studentList = new ArrayList<>();
			for(User eachUser : users) {
				String name = eachUser.getFirstName()+ " "+eachUser.getLastName();
				studentList.add(name);
			}
			cpd.setStudentNames(studentList);
			courseList.add(cpd);
		}
		
		return ResponseEntity.ok(courseList);
	}	
	
	
}
