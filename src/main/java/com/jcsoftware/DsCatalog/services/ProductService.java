package com.jcsoftware.DsCatalog.services;

import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.jcsoftware.DsCatalog.dtos.CategoryDTO;
import com.jcsoftware.DsCatalog.dtos.ProductDTO;
import com.jcsoftware.DsCatalog.dtos.ProductInsertDTO;
import com.jcsoftware.DsCatalog.dtos.UriRecord;
import com.jcsoftware.DsCatalog.entities.Category;
import com.jcsoftware.DsCatalog.entities.Product;
import com.jcsoftware.DsCatalog.projections.ProductProjection;
import com.jcsoftware.DsCatalog.repositories.ProductRepository;
import com.jcsoftware.DsCatalog.services.exceptions.IntegrityViolationException;
import com.jcsoftware.DsCatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;

@Service
public class ProductService {
	
	@Autowired
	private ProductRepository repository;
	
	@Autowired
	private S3Service s3Service;
	
	/*
	public Page<ProductDTO> findAllPaged(Pageable pageable) {

	   Page<Product> products = repository.findAll(pageable);
			
	   return products.map(x -> new ProductDTO(x));
	   
	}
	*/
	
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(String name,String categoryId,Pageable pageable) {
		
		
		List<Long> categoryIds = Arrays.asList();
		
		if(categoryId.length()>0)
			categoryIds = Arrays.asList(categoryId.split(",")).stream().map(x-> Long.parseLong(x)).toList();
		/*
		Page<ProductProjection> page = repository.searchProducts(categoryIds,name, pageable);
		List<Long> productIds = page.map(x->x.getId()).toList();
		List<Product> products = repository.searchProductsWithCategories(productIds);
		List<ProductDTO> productsDto = products.stream().map(p-> new ProductDTO(p,p.getCategories())).toList();
		return  new PageImpl<>(productsDto,page.getPageable(),page.getTotalElements());
		*/
		
		Page<ProductProjection> page = repository.searchProducts(categoryIds, name, pageable);
	    List<Long> productIds = page.map(ProductProjection::getId).toList();
	    List<Product> products = repository.searchProductsWithCategories(productIds);
	    List<ProductDTO> productsDto = products.stream().map(p -> new ProductDTO(p, p.getCategories())).toList();
	    Comparator<ProductDTO> comparator = buildComparator(pageable.getSort());
	    if (comparator != null) {
	        productsDto = productsDto.stream().sorted(comparator).toList();
	    }
	    return new PageImpl<>(productsDto, pageable, page.getTotalElements());
	    
	}
	
	@Transactional
	public ProductDTO insert(ProductDTO dto) {

		Product newProduct = new Product();
		copyDtoToEntity(dto, newProduct);
		
		newProduct = repository.save(newProduct);

		return new ProductDTO(newProduct);
	}
	
	@Transactional
	public ProductDTO insert2(ProductInsertDTO dto,MultipartFile file) {
		URL url = s3Service.uploadFile(file);
		Product newProduct = new Product();
		copyDtoToEntity2(dto, newProduct);
		newProduct.setImgUrl(url.toString());
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
	
	private void copyDtoToEntity2(ProductInsertDTO dto, Product entity) {

		entity.setName(dto.getName());
		entity.setDescription(dto.getDescription());
		entity.setPrice(dto.getPrice());
		entity.setDate(dto.getDate());
		entity.getCategories().clear();
		for(CategoryDTO catDTO: dto.getCategories()) {
			Category cat = new Category();
			cat.setId(catDTO.getId());
			entity.getCategories().add(cat);
		}
		
	}
	
	/*
	private Comparator<ProductDTO> buildComparator(Sort sort) {
	    Comparator<ProductDTO> comparator = null;

	    for (Sort.Order order : sort) {
	        Comparator<ProductDTO> current;

	        // Suporte para campos que você quer permitir ordenação (ex: name)
	        if (order.getProperty().equalsIgnoreCase("name")) {
	            current = Comparator.comparing(ProductDTO::getName, String.CASE_INSENSITIVE_ORDER);
	        } else if (order.getProperty().equalsIgnoreCase("id")) {
	            current = Comparator.comparing(ProductDTO::getId);
	        } else {
	            continue; // Ignora ordenações por campos desconhecidos
	        }

	        if (order.getDirection() == Sort.Direction.DESC) {
	            current = current.reversed();
	        }

	        comparator = comparator == null ? current : comparator.thenComparing(current);
	    }

	    return comparator;
	}
*/
	private Comparator<ProductDTO> buildComparator(Sort sort) {
	    Comparator<ProductDTO> comparator = null;

	    for (Sort.Order order : sort) {
	        Comparator<ProductDTO> current;

	        switch (order.getProperty().toLowerCase()) {
	            case "name":
	                current = Comparator.comparing(ProductDTO::getName, String.CASE_INSENSITIVE_ORDER);
	                break;
	            case "id":
	                current = Comparator.comparing(ProductDTO::getId);
	                break;
	            case "price":
	                current = Comparator.comparing(ProductDTO::getPrice); // Supondo que seja BigDecimal ou Double
	                break;
	            default:
	                continue; // Ignora campos não suportados
	        }

	        if (order.getDirection() == Sort.Direction.DESC) {
	            current = current.reversed();
	        }

	        comparator = comparator == null ? current : comparator.thenComparing(current);
	    }

	    return comparator;
	}

	public UriRecord uploadFile(MultipartFile file) {
		URL url = s3Service.uploadFile(file);
		return new UriRecord(url.toString());
	}

	


	
}
