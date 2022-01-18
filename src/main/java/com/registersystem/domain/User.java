package com.registersystem.domain;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "user", uniqueConstraints={@UniqueConstraint(columnNames={"username"})})
@Getter
@Setter
@ToString
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int id;
	
	@Column(name = "username")
	private String username; 
	
	private String password;
	private String firstName;
	private String lastName;
	
	@OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Role role; 
	
	@ManyToMany(cascade = CascadeType.ALL)
	private Set<Course> courses;
	
}
