package com.jcsoftware.DsCatalog.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class NewPasswordDTO {

	@NotBlank(message = "Campo requerido")
	private String token;
	
	@NotBlank(message = "Campo requerido")
	@Size(min = 8, message = "O campo deve ter pelo menos 8 caracteres")
	private String password;
	
	public NewPasswordDTO() {
		
	}
	

	public NewPasswordDTO(String token,	String password) {
		super();
		this.token = token;
		this.password = password;
	}


	public String getToken() {
		return token;
	}


	public void setToken(String token) {
		this.token = token;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	
	
	
}
