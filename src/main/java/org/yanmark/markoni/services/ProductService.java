package org.yanmark.markoni.services;

import org.yanmark.markoni.domain.models.services.ProductServiceModel;

import java.util.List;

public interface ProductService {
	
	ProductServiceModel saveProduct(ProductServiceModel productService);

	void deleteProduct(String id);
	
	List<ProductServiceModel> getAllProducts();
	
	ProductServiceModel getProductByName(String name);
	
	ProductServiceModel getProductById(String id);
	
	List<ProductServiceModel> getAllProductsByName(String name);
}
