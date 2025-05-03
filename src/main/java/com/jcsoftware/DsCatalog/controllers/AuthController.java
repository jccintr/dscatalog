package com.jcsoftware.DsCatalog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jcsoftware.DsCatalog.dtos.EmailDTO;
import com.jcsoftware.DsCatalog.dtos.NewPasswordDTO;
import com.jcsoftware.DsCatalog.dtos.UserDTO;
import com.jcsoftware.DsCatalog.services.AuthService;
import com.jcsoftware.DsCatalog.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
	
	@Autowired
	private AuthService service;
	
	@Autowired
	private UserService userService;

	
	
	@PostMapping(value="recovery-token")
	public ResponseEntity<Void> createRecoveryToken(@Valid @RequestBody EmailDTO dto){
		service.createRecoveryToken(dto);
		return ResponseEntity.noContent().build();
	}
	
	
	@PutMapping(value="new-password")
	public ResponseEntity<Void> saveNewPassword(@Valid @RequestBody NewPasswordDTO dto){
		service.saveNewPassword(dto);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OPERATOR')")
	@GetMapping(value="/me")
	public ResponseEntity<UserDTO> findById(){
		UserDTO userDTO = userService.findMe();
		return ResponseEntity.ok().body(userDTO);
	}
	
	
	
	
	
	
	


}
