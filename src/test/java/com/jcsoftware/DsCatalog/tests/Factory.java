package com.jcsoftware.DsCatalog.tests;

import java.time.Instant;

import com.jcsoftware.DsCatalog.dtos.ProductDTO;
import com.jcsoftware.DsCatalog.entities.Category;
import com.jcsoftware.DsCatalog.entities.Product;

public class Factory {
	
	public static Product createProduct() {
		Product product = new Product(1L,"TV 50pol","Super tv colorida e fininha", 300.0, "http://www",Instant.parse("2025-01-31T13:13:00Z"));
		product.getCategories().add(new Category(1L,"Eletrônicos"));
		return product;
	}
	
	public static ProductDTO createProductDTO() {
		Product product = createProduct();
		return new ProductDTO(product,product.getCategories());
	}
	
	public static Category createCategory() {
		return new Category(1L,"Eletrônicos");
	}

}
