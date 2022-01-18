package com.registersystem.domain;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name="courses")
@Getter
@Setter
@ToString
public class Course {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)	
	private int id;
	@NotNull
	private String courseName;
	
	@NotNull
	private String description;
	@NotNull
	private int capacity;
	
	public Course () {
	
	}
	public Course(int id, String courseName, String description, int capacity) {
		super();
		this.id = id;
		this.courseName = courseName;
		this.description = description;
		this.capacity = capacity;
	}
	
}
