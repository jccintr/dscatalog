package com.jcsoftware.DsCatalog.controllers;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcsoftware.DsCatalog.dtos.ProductDTO;
import com.jcsoftware.DsCatalog.services.ProductService;
import com.jcsoftware.DsCatalog.services.exceptions.ResourceNotFoundException;
import com.jcsoftware.DsCatalog.tests.Factory;

//@WebMvcTest(value = ProductController.class)
@WebMvcTest(value = ProductController.class,excludeAutoConfiguration = {SecurityAutoConfiguration.class})
public class ProductControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
	private ProductService service;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	private ProductDTO productDTO;
	private PageImpl<ProductDTO> page;
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	
	
	
	@BeforeEach
	void setup() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		dependentId = 10L;
		productDTO = Factory.createProductDTO();
		page = new PageImpl<>(List.of(productDTO));
		
	  //  when(service.findAllPaged(ArgumentMatchers.any())).thenReturn(page);
	    
	    when(service.findById(existingId)).thenReturn(productDTO);
	    when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);
	    
	  //  when(service.update(eq(existingId),any())).thenReturn(productDTO);
	  //  when(service.update(eq(nonExistingId),any())).thenThrow(ResourceNotFoundException.class);
	    
	    when(service.update(existingId,productDTO)).thenReturn(productDTO);
	    when(service.update(nonExistingId,productDTO)).thenThrow(ResourceNotFoundException.class);
	    
	    doNothing().when(service).delete(existingId);
	    doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
		doThrow(DataIntegrityViolationException.class).when(service).delete(dependentId);
		
		when(service.insert(productDTO)).thenReturn(productDTO);
	}
	
	@Test
	public void deleteShouldShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		ResultActions result = mockMvc.perform( delete("/products/{id}",nonExistingId) );
		result.andExpect(status().isNotFound());
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() throws Exception {
		ResultActions result = mockMvc.perform( delete("/products/{id}",existingId) );
		result.andExpect(status().isNoContent());
	}
	
	@Test
	public void insertShoulReturnProductDTOAndCreatedStatus() throws Exception {
		
		String requestBody = objectMapper.writeValueAsString(productDTO);
		ResultActions result = mockMvc.perform( post("/products")
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON) );
		result.andExpect(status().isCreated());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
		
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() throws Exception {
		
		String requestBody = objectMapper.writeValueAsString(productDTO);
		ResultActions result = mockMvc.perform( put("/products/{id}",existingId)
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON) );
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
		
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		
		String requestBody = objectMapper.writeValueAsString(productDTO);
		ResultActions result = mockMvc.perform( put("/products/{id}",nonExistingId)
				.content(requestBody)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON) );
		result.andExpect(status().isNotFound());
		
	}
	
	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() throws Exception {
		
		ResultActions result = mockMvc.perform( get("/products/{id}",existingId).accept(MediaType.APPLICATION_JSON) );
		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").exists());
		result.andExpect(jsonPath("$.name").exists());
		result.andExpect(jsonPath("$.description").exists());
	}
	
	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception {
		ResultActions result = mockMvc.perform(get("/products/{id}",nonExistingId).accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isNotFound());
	}
	
	
	@Test
	public void findAllShouldReturnPage() throws Exception {
		//mockMvc.perform(get("/products")).andExpect(status().isOk()); ou 
		
		ResultActions result = mockMvc.perform(get("/products").accept(MediaType.APPLICATION_JSON));
		result.andExpect(status().isOk());
	}
}
