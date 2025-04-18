package com.jcsoftware.DsCatalog.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jcsoftware.DsCatalog.entities.Product;
import com.jcsoftware.DsCatalog.projections.ProductProjection;


@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

	@Query(nativeQuery = true, value = """
			SELECT * FROM (
			SELECT DISTINCT products.id, products.name
			FROM products
			INNER JOIN product_category ON product_category.product_id = products.id
			WHERE (:categoryIds IS NULL OR product_category.category_id IN (:categoryIds))
			AND (LOWER(products.name) LIKE LOWER(CONCAT('%',:name,'%')))
			
			) AS tb_result
			
			""",
			countQuery = """
			SELECT COUNT(*) FROM (
			SELECT DISTINCT products.id, products.name
			FROM products
			INNER JOIN product_category ON product_category.product_id = products.id
			WHERE (:categoryIds IS NULL OR product_category.category_id IN (:categoryIds))
			AND (LOWER(products.name) LIKE LOWER(CONCAT('%',:name,'%')))
			) AS tb_result
			""")
	Page<ProductProjection> searchProducts(List<Long> categoryIds, String name, Pageable pageable);
	
	
	
	
	@Query("SELECT obj FROM Product obj JOIN FETCH obj.categories "
			+ "WHERE obj.id IN :productIds")
	List<Product> searchProductsWithCategories(List<Long> productIds);

	

}
