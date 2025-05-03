package com.jcsoftware.DsCatalog.projections;

public interface ProductProjection extends IdProjection<Long> {
	
	
	String getName();
	Double getPrice();

}
