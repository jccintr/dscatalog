package com.jcsoftware.DsCatalog.controllers;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.jcsoftware.DsCatalog.dtos.ProductDTO;
import com.jcsoftware.DsCatalog.dtos.UriRecord;
import com.jcsoftware.DsCatalog.entities.Product;
import com.jcsoftware.DsCatalog.services.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/products")
public class ProductController {
	
	@Autowired
	private ProductService service;

	@GetMapping
	public ResponseEntity<Page<ProductDTO>> findAllPaged(String name,String categoryId,Pageable pageable){
		Page<ProductDTO> products = service.findAllPaged(name,categoryId,pageable);
        return ResponseEntity.ok().body(products);
	}
	
	/*
	@GetMapping
	public ResponseEntity<Page<ProductDTO>> findAllPaged(Pageable pageable){
		Page<ProductDTO> products = service.findAllPaged(pageable);
        return ResponseEntity.ok().body(products);
	}
	*/
	
	@GetMapping(value="/{id}")
	public ResponseEntity<ProductDTO> findById(@PathVariable Long id){
		ProductDTO productDTO = service.findById(id);
		return ResponseEntity.ok().body(productDTO);
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_OPERATOR')")	
	@PostMapping
	public ResponseEntity<ProductDTO> insert(@Valid @RequestBody ProductDTO dto){
		
		ProductDTO newProductDTO = service.insert(dto);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
				.buildAndExpand(newProductDTO.getId()).toUri();
		
		return ResponseEntity.created(uri).body(newProductDTO);
	}
	
	
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@PutMapping(value="/{id}")
	public ResponseEntity<ProductDTO> update(@PathVariable Long id,@Valid @RequestBody ProductDTO dto){
		
		var  product = service.update(id, dto);
		return ResponseEntity.ok().body(product);
		
	}
	
	
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Product> delete(@PathVariable Long id){
		
		service.delete(id);
		
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_OPERATOR')")
	@PostMapping(value = "/image")
	public ResponseEntity<UriRecord> uploadImage(@RequestParam("file") MultipartFile file) {
		UriRecord dto = service.uploadFile(file);
		return ResponseEntity.ok().body(dto);
	}


}
