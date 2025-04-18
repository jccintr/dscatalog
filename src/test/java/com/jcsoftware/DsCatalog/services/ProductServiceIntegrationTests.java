package com.jcsoftware.DsCatalog.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.jcsoftware.DsCatalog.repositories.ProductRepository;
import com.jcsoftware.DsCatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional
public class ProductServiceIntegrationTests {
	
	@Autowired
	private ProductService service; 
	@Autowired
	private ProductRepository repository;
	
	private Long existingId;
	private Long nonExistingId;
	private Long countTotalProducts;
	
	@BeforeEach
	void setup() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 100L;
	}
	
	@Test
	public void deleteShouldDeleteRessourceWhenIdExists() {
	    service.delete(existingId);	
	    Assertions.assertEquals(countTotalProducts-1,repository.count());
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExists() {
		Assertions.assertThrows(ResourceNotFoundException.class,()->{
			service.delete(nonExistingId);
		});
	}
	/*
	@Test
	public void findAllPagedShouldReturnPageWhenPage0to10() {
		
		PageRequest pageRequest =  PageRequest.of(0, 10);
		//Pageable pageable = PageRequest.of(0, 10);
		Page<ProductDTO> page = service.findAllPaged(pageRequest);
		
		Assertions.assertTrue(!page.isEmpty());
		Assertions.assertEquals(0, page.getNumber());
		Assertions.assertEquals(10,page.getSize());
		Assertions.assertEquals(countTotalProducts,page.getTotalElements());
	}
	*/
	/*
	@Test
	public void findAllPagedShouldReturnEmptyPageWhenPageDoesNotExists() {
		
		PageRequest pageRequest =  PageRequest.of(110, 120);
		//Pageable pageable = PageRequest.of(110, 120);
		Page<ProductDTO> page = service.findAllPaged(pageRequest);
		
		Assertions.assertTrue(page.isEmpty());
		
	}
	*/
	/*
	@Test
	public void findAllPagedShouldReturnOrderedPageWhenSortedByName() {
		
		PageRequest pageRequest =  PageRequest.of(0, 10,Sort.by("Name"));
		Page<ProductDTO> page = service.findAllPaged(pageRequest);
		Assertions.assertTrue(!page.isEmpty());
		Assertions.assertEquals("3D Night Light", page.getContent().get(0).getName());
		Assertions.assertEquals("Adjustable Yoga Mat Strap", page.getContent().get(2).getName());
	}
	*/

}
