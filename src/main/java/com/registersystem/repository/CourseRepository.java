package com.registersystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.registersystem.domain.Course;
import com.registersystem.domain.User;

@Repository
public interface CourseRepository extends JpaRepository<Course, Integer>{
	public List<Course> findByCourseNameContains(String courseName);
	public Course findById(int id);
}
