package com.feedback.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.feedback.entity.Register;

@Repository
public interface IRegisterRepository extends JpaRepository<Register, Integer> {
	
	public Register findByUsername(String username);
	
	@Query("SELECT r FROM Register r WHERE r.username = :username AND r.password = :password")
	Register findByRegisterUsernameAndPassword(String username, String password);
}