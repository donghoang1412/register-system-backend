package com.registersystem.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.registersystem.domain.Course;
import com.registersystem.repository.CourseRepository;

@Service
public class CourseServiceImpl implements CourseService{

	@Autowired
	CourseRepository courseRepository;
	
	@Override
	public Course saveCourse(Course course) {
		return courseRepository.save(course);
	}

	@Override
	public List<Course> getCourse() {
		return courseRepository.findAll();
	}

	@Override
	public List<Course> getCoursesByNameContains(String courseName) {
		return courseRepository.findByCourseNameContains(courseName);
	}

	@Override
	public Course getCourseById(int id) {
		return courseRepository.findById(id);
	}

	@Override
	public void deleteById(int id) {
		courseRepository.deleteById(id);
	}

	

}
