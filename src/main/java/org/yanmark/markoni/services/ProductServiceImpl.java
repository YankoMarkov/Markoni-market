package org.yanmark.markoni.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yanmark.markoni.domain.entities.Product;
import org.yanmark.markoni.domain.models.bindings.products.ProductCreateBindingModel;
import org.yanmark.markoni.domain.models.services.CategoryServiceModel;
import org.yanmark.markoni.domain.models.services.ProductServiceModel;
import org.yanmark.markoni.domain.models.services.UserServiceModel;
import org.yanmark.markoni.repositories.ProductRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CategoryService categoryService;
    private final UserService userService;
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryService categoryService,
                              UserService userService,
                              CloudinaryService cloudinaryService,
                              ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.userService = userService;
        this.cloudinaryService = cloudinaryService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductServiceModel saveProduct(ProductServiceModel productService,
                                           ProductCreateBindingModel productCreate) throws IOException {
        Set<CategoryServiceModel> categoriesServiceModels = this.categoryService.getAllCategories().stream()
                .filter(category -> productCreate.getCategories().contains(category.getId()))
                .collect(Collectors.toSet());
        productService.setCategories(categoriesServiceModels);
        productService.setImage(this.cloudinaryService.uploadImage(productCreate.getImage()));
        return saveProductService(productService);
    }

    @Override
    public ProductServiceModel editProduct(ProductServiceModel productService) {
        return saveProductService(productService);
    }

    @Override
    public void deleteProduct(String id) {
        try {
            this.productRepository.deleteById(id);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public List<ProductServiceModel> getAllProducts() {
        List<Product> products = this.productRepository.findAllOrdered();
        return takeProductServices(products);
    }

    @Override
    public ProductServiceModel getProductByName(String name) {
        Product product = this.productRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Product was not found!"));
        return this.modelMapper.map(product, ProductServiceModel.class);
    }

    @Override
    public ProductServiceModel getProductById(String id) {
        Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product was not found!"));
        return this.modelMapper.map(product, ProductServiceModel.class);
    }

    @Override
    public List<ProductServiceModel> getAllProductsByName(String name) {
        List<Product> products = this.productRepository.findAllByName(name);
        return takeProductServices(products);
    }

    private ProductServiceModel saveProductService(ProductServiceModel productService) {
        Product product = this.modelMapper.map(productService, Product.class);
        try {
            product = this.productRepository.saveAndFlush(product);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return this.modelMapper.map(product, ProductServiceModel.class);
    }

    private List<ProductServiceModel> takeProductServices(List<Product> products) {
        if (products == null) {
            throw new IllegalArgumentException("Products was not found!");
        }
        return products.stream()
                .map(product -> {
                    if (product.getDescription().length() > 30) {
                        product.setDescription(product.getDescription().substring(0, 30) + "...");
                    }
                    return this.modelMapper.map(product, ProductServiceModel.class);
                })
                .collect(Collectors.toUnmodifiableList());
    }
}
