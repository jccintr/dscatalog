package com.jcsoftware.DsCatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jcsoftware.DsCatalog.dtos.CategoryDTO;
import com.jcsoftware.DsCatalog.dtos.ProductDTO;
import com.jcsoftware.DsCatalog.entities.Category;
import com.jcsoftware.DsCatalog.entities.Product;
import com.jcsoftware.DsCatalog.repositories.ProductRepository;
import com.jcsoftware.DsCatalog.services.exceptions.IntegrityViolationException;
import com.jcsoftware.DsCatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
	
	public Page<ProductDTO> findAllPaged(Pageable pageable) {

	   Page<Product> products = repository.findAll(pageable);
			
	   return products.map(x -> new ProductDTO(x));
	   
	}
	
	
	@Transactional
	public ProductDTO insert(ProductDTO dto) {

		Product newProduct = new Product();
		copyDtoToEntity(dto, newProduct);
		
		newProduct = repository.save(newProduct);

		return new ProductDTO(newProduct);
	}
	
	
	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {

		Optional<Product> productO = repository.findById(id);
		Product product = productO.orElseThrow(() -> new ResourceNotFoundException(id));
		
		ProductDTO dto = new ProductDTO(product,product.getCategories());
		
		return dto;

	}
	
	
	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {

		try {
			Product product = repository.getReferenceById(id);
			copyDtoToEntity(dto, product);
			product = repository.save(product);
			return new ProductDTO(product);
		} catch (EntityNotFoundException e) {
			throw (new ResourceNotFoundException(id));
		}

	}
	
	
	@Transactional(propagation=Propagation.SUPPORTS)
	public void delete(Long id) {

		try {

			if (repository.existsById(id)) {
				repository.deleteById(id);
			} else {
				throw (new ResourceNotFoundException(id));
			}
		}

		catch (DataIntegrityViolationException e) {
			throw (new IntegrityViolationException(id));
		}

	}
	
	
	private void copyDtoToEntity(ProductDTO dto, Product entity) {

		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setPrice(dto.getPrice());
		entity.setImgUrl(dto.getImgUrl());
		entity.setDate(dto.getDate());
		entity.getCategories().clear();
		for(CategoryDTO catDTO: dto.getCategories()) {
			Category cat = new Category();
			cat.setId(catDTO.getId());
			entity.getCategories().add(cat);
		}
		
	}
}
