package com.jcsoftware.DsCatalog.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.jcsoftware.DsCatalog.entities.Category;

public interface CategoryRepository extends JpaRepository<Category,Long>{
	
	@Query("SELECT p FROM Category p "
			+ " WHERE UPPER(p.name) LIKE UPPER(CONCAT('%',:name,'%'))")
	Page<Category> searchByName(String name,Pageable pageable);

}
