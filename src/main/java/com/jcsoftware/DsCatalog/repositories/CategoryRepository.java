package com.jcsoftware.DsCatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jcsoftware.DsCatalog.entities.Category;

public interface CategoryRepository extends JpaRepository<Category,Long>{

}
