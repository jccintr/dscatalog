package com.jcsoftware.DsCatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jcsoftware.DsCatalog.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category,Long>{
	
	/*
	@Query("SELECT p FROM Category p "
			+ " WHERE UPPER(p.name) LIKE UPPER(CONCAT('%',:name,'%'))")
	Page<Category> searchByName(String name,Pageable pageable);
*/
}
