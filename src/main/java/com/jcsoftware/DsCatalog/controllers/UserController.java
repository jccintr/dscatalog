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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jcsoftware.DsCatalog.dtos.UserDTO;
import com.jcsoftware.DsCatalog.dtos.UserInsertDTO;
import com.jcsoftware.DsCatalog.dtos.UserUpdateDTO;
import com.jcsoftware.DsCatalog.entities.User;
import com.jcsoftware.DsCatalog.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/users")
public class UserController {
	
	@Autowired
	private UserService service;

	
	@GetMapping
	public ResponseEntity<Page<UserDTO>> findAllPaged(Pageable pageable){
		Page<UserDTO> users = service.findAllPaged(pageable);
        return ResponseEntity.ok().body(users);
	}
	
	
	
	@PostMapping
	public ResponseEntity<UserDTO> insert(@Valid @RequestBody UserInsertDTO dto){
		
		UserDTO newUserDTO = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(newUserDTO.getId()).toUri();
		
		return ResponseEntity.created(uri).body(newUserDTO);
	}
	
	@GetMapping(value="/{id}")
	public ResponseEntity<UserDTO> findById(@PathVariable Long id){
		UserDTO userDTO = service.findById(id);
		return ResponseEntity.ok().body(userDTO);
	}
	
	//@PreAuthorize("hasRole('ROLE_ADMIN')")
	
	@PutMapping(value="/{id}")
	public ResponseEntity<UserDTO> update(@Valid @PathVariable Long id,@Valid @RequestBody UserUpdateDTO dto){
		
		var user = service.update(id, dto);
		return ResponseEntity.ok().body(user);
		
	}
	
	
	//@PreAuthorize("hasRole('ROLE_ADMIN')")
	
	@DeleteMapping(value="/{id}")
	public ResponseEntity<User> delete(@PathVariable Long id){
		
		service.delete(id);
		
		return ResponseEntity.noContent().build();
	}


}
