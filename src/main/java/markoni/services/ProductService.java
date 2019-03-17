package markoni.services;

import markoni.models.services.ProductServiceModel;

import java.util.List;

public interface ProductService {
	
	ProductServiceModel saveProduct(ProductServiceModel productService);
	
	List<ProductServiceModel> getAllProducts();
	
	ProductServiceModel getProductByName(String name);
	
	List<ProductServiceModel> getAllProductsByName(String name);
	
	boolean productExist(String name);
}
