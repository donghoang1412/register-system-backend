package com.registersystem.service;

import java.util.List;

import com.registersystem.domain.Course;

public interface CourseService {
	public Course saveCourse(Course course);
	public List<Course> getCourse();
	public List<Course> getCoursesByNameContains(String courseName);
	public Course getCourseById (int id);
	public void deleteById(int id);
}
