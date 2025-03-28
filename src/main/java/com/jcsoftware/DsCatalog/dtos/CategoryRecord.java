package com.jcsoftware.DsCatalog.dtos;

import com.jcsoftware.DsCatalog.entities.Category;

public record CategoryRecord(Long id,String name) {

	public CategoryRecord(Category entity) {
		this(entity.getId(),entity.getName());
	}
}
