package com.registersystem.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CourseDisplay {
	private int id;
	private String courseName;
	private String profName;
	private int capacity;
	private String description;
}
