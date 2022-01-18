package com.registersystem.repository;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.registersystem.domain.User;

@Transactional
@Repository
public interface UserRepository extends JpaRepository<User, Integer>{
	public User findByUsername(String username);
	public List<User> findByUsernameContains (String username);
	public List<User> findByFirstNameContains (String firstName);
	
	@Query(value="select * from user where id in (select User_id from user_courses where courses_id=:courseId)", nativeQuery = true)
	public List<User> findUserByCourses(@Param("courseId") int courseId);
	
	@Query(value="select * from user where id=:userId", nativeQuery = true)
	public User findUserById(@Param("userId") int userId);

}
