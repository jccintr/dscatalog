package com.jcsoftware.DsCatalog.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class EmailDTO {
	
	@NotBlank(message = "Campo requerido")
	@Email(message="Email inválido")
    private String email;

	public EmailDTO() {
		
	}
	
	
	public EmailDTO(String email) {
		super();
		this.email = email;
	}


	public String getEmail() {
		return email;
	}

	
	
	
	/*
	@NotBlank
    @Email
    private String to;
    
	@NotBlank
    private String subject;
    
    @NotBlank
    private String body;
    
    public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
*/
}
