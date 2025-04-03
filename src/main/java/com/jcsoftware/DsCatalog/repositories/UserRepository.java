package com.jcsoftware.DsCatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jcsoftware.DsCatalog.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

	
	public User findByEmail(String email);

}
