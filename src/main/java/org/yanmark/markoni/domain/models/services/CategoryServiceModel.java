package org.yanmark.markoni.domain.models.services;

import java.util.HashSet;
import java.util.Set;

public class CategoryServiceModel extends BaseServiceModel {

	private String name;
	private Set<ProductServiceModel> products;
	
	public CategoryServiceModel() {
		this.products = new HashSet<>();
	}

	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public Set<ProductServiceModel> getProducts() {
		return this.products;
	}
	
	public void setProducts(Set<ProductServiceModel> products) {
		this.products = products;
	}
}
