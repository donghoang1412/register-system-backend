package com.registersystem.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseProfessorDisplay {
	
	private int id;
	private String courseName;
	private int capacity;
	private String description;
	private List<String> studentNames;
}
