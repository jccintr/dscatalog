package com.jcsoftware.DsCatalog.repositories;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.jcsoftware.DsCatalog.entities.Product;
import com.jcsoftware.DsCatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {
	
	@Autowired
	private ProductRepository repository;
	private Long existingId;
	private Long notExistingId;
	private Long countTotalProducts;
	
	@BeforeEach
	void setup() throws Exception {
		existingId = 1L;
		notExistingId = 1000L;
		countTotalProducts = 100L;
	}
	
	@Test
	public void deleteSholdDeleteObjectWhenIdExists() {
		
		
		repository.deleteById(existingId);
		Optional<Product> productO = repository.findById(existingId);
		Assertions.assertFalse(productO.isPresent());
		
	}
	
	@Test
	public void saveShouldPersistObjectWhenIdIsNull() {
		Product product = Factory.createProduct();
		product.setId(null);
		product = repository.save(product);
		Assertions.assertNotNull(product.getId());
		Assertions.assertEquals(countTotalProducts + 1,product.getId());
		
	}
	
	@Test
	public void findByIdShouldReturnEmptyOptionalWhenIdNotExists() {
		Optional<Product> productO = repository.findById(notExistingId);
		Assertions.assertTrue(productO.isEmpty());
	}
	
	@Test
	public void findByIdShouldReturnNonEmptyOptionalWhenIdExists() {
		Optional<Product> productO = repository.findById(existingId);
		Assertions.assertTrue(productO.isPresent());
	}

}
