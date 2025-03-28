package com.jcsoftware.DsCatalog.dtos;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.jcsoftware.DsCatalog.entities.Category;
import com.jcsoftware.DsCatalog.entities.Product;

public class ProductDTO {

	private Long id;

//	@NotBlank(message = "Campo requerido")
//	@Size(min = 3, max = 80, message = "O campo deve ter mais de 3 e menos de 80 caracteres")
	private String name;
//	@Size(min = 10, message = "A descrição deve ter mais do que 10 caracteres.")
	private String description;
//	@NotNull(message="Campo requerido")
//	@Positive(message = "O preço deve ser maior do que zero.")
	private Double price;
	private String imgUrl;
//	@NotEmpty(message = "Deve ser informada pelo menos uma categoria.")
	private List<CategoryDTO> categories = new ArrayList<>();

	public ProductDTO() {

	}
	
	

	public ProductDTO(Long id, String name, String description, Double price, String imgUrl) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.imgUrl = imgUrl;
	}



	public ProductDTO(Product entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.description = entity.getDescription();
		this.price = entity.getPrice();
		this.imgUrl = entity.getImgUrl();
		
	}
	
	public ProductDTO(Product entity,Set<Category> categories) {
		this(entity);
		categories.forEach(cat->this.categories.add(new CategoryDTO(cat)));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	public List<CategoryDTO> getCategories() {
		return categories;
	}
	
	

}

