package com.jcsoftware.DsCatalog.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jcsoftware.DsCatalog.dtos.CategoryDTO;
import com.jcsoftware.DsCatalog.entities.Category;
import com.jcsoftware.DsCatalog.services.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {
	
	@Autowired
	private CategoryService service;

	@GetMapping
	public ResponseEntity<Page<CategoryDTO>> findAll(@RequestParam(defaultValue="") String name,Pageable pageable){
		Page<CategoryDTO> categories = service.findAll(name,pageable);
        return ResponseEntity.ok().body(categories);
	}
	
	@PostMapping
	public ResponseEntity<CategoryDTO> insert(@RequestBody CategoryDTO dto){
		
		CategoryDTO newCategoryDTO = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(newCategoryDTO.getId()).toUri();
		
		return ResponseEntity.created(uri).body(newCategoryDTO);
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<CategoryDTO> findById(@PathVariable Long id){
		CategoryDTO categoryDTO = service.findById(id);
		return ResponseEntity.ok().body(categoryDTO);
	}
	
	//@PreAuthorize("hasRole('ROLE_ADMIN')")
	
	@PutMapping(value="/{id}")
	public ResponseEntity<CategoryDTO> update(@PathVariable Long id,@Valid @RequestBody CategoryDTO dto){
		
		var  category = service.update(id, dto);
		return ResponseEntity.ok().body(category);
		
	}
	
	
	//@PreAuthorize("hasRole('ROLE_ADMIN')")
	
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Category> delete(@PathVariable Long id){
		
		service.delete(id);
		
		return ResponseEntity.noContent().build();
	}


}
