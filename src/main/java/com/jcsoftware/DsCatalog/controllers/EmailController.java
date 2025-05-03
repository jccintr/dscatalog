package com.jcsoftware.DsCatalog.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.jcsoftware.DsCatalog.dtos.EmailDTO;
import com.jcsoftware.DsCatalog.services.EmailService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/email")
public class EmailController {

	@Autowired
	private EmailService emailService;
	
	/*
	@PostMapping
	public ResponseEntity<Void> sendEmail(@Valid @RequestBody EmailDTO obj) {
		emailService.sendEmail(obj.getTo(),obj.getSubject(),obj.getBody());
		return ResponseEntity.noContent().build();
	}
	*/
}