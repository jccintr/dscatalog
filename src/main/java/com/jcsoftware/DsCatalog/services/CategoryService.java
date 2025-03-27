package com.jcsoftware.DsCatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jcsoftware.DsCatalog.dtos.CategoryDTO;
import com.jcsoftware.DsCatalog.entities.Category;
import com.jcsoftware.DsCatalog.repositories.CategoryRepository;
import com.jcsoftware.DsCatalog.services.exceptions.IntegrityViolationException;
import com.jcsoftware.DsCatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class CategoryService {
	
	@Autowired
	private CategoryRepository repository;
	
	public Page<CategoryDTO> findAll(String name,Pageable pageable) {

		//List<Category> categories = repository.findAll();
		
		//return categories.stream().map(x -> new CategoryDTO(x)).toList();

       Page<Category> categories = repository.searchByName(name,pageable);
		
		return categories.map(x -> new CategoryDTO(x));
	}
	
	
	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {

		Category newCat = new Category();
		copyDtoToEntity(dto, newCat);
		
		newCat = repository.save(newCat);

		return new CategoryDTO(newCat);
	}
	
	
	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {

		Optional<Category> categoryO = repository.findById(id);
		Category category = categoryO.orElseThrow(() -> new ResourceNotFoundException(id));
		
		CategoryDTO dto = new CategoryDTO(category);
		
		return dto;

	}
	
	
	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {

		try {
			Category category = repository.getReferenceById(id);
			copyDtoToEntity(dto, category);
			category = repository.save(category);
			return new CategoryDTO(category);
		} catch (EntityNotFoundException e) {
			throw (new ResourceNotFoundException(id));
		}

	}
	
	
	@Transactional(propagation=Propagation.SUPPORTS)
	public void delete(Long id) {

		try {

			if (repository.existsById(id)) {
				repository.deleteById(id);
			} else {
				throw (new ResourceNotFoundException(id));
			}
		}

		catch (DataIntegrityViolationException e) {
			throw (new IntegrityViolationException(id));
		}

	}
	
	
	private void copyDtoToEntity(CategoryDTO dto, Category entity) {

		entity.setName(dto.getName());
		
		
	}
}
