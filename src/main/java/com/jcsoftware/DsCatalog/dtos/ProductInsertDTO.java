package com.jcsoftware.DsCatalog.dtos;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.jcsoftware.DsCatalog.entities.Category;
import com.jcsoftware.DsCatalog.entities.Product;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ProductInsertDTO {
	
	private Long id;

	@NotBlank(message = "Campo requerido")
	@Size(min = 3, max = 80, message = "O campo deve ter mais de 3 e menos de 80 caracteres")
	private String name;

	private String description;
	@NotNull(message="Campo requerido")
	@Positive(message = "O preço deve ser maior do que zero.")
	private Double price;
	
	private String imgUrl;
	@Column(columnDefinition="TIMESTAMP WITHOUT TIME ZONE")
	private Instant date;
	@NotEmpty(message = "Deve ser informada pelo menos uma categoria.")
	private List<CategoryDTO> categories = new ArrayList<>();
	
	public ProductInsertDTO() {

	}
	
	public ProductInsertDTO(Long id, String name, String description, Double price, String imgUrl,Instant date) {
		super();
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.imgUrl = imgUrl;
		this.date = date;
	}



	public ProductInsertDTO(Product entity) {
		this.id = entity.getId();
		this.name = entity.getName();
		this.description = entity.getDescription();
		this.price = entity.getPrice();
		this.imgUrl = entity.getImgUrl();
		this.date = entity.getDate();
		
	}
	
	public ProductInsertDTO(Product entity,Set<Category> categories) {
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

	public Instant getDate() {
		return date;
	}

	public void setDate(Instant date) {
		this.date = date;
	}

	public List<CategoryDTO> getCategories() {
		return categories;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ProductInsertDTO other = (ProductInsertDTO) obj;
		return Objects.equals(id, other.id);
	}
	
	
	


}
