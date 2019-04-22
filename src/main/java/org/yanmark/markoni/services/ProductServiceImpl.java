package org.yanmark.markoni.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yanmark.markoni.domain.entities.Product;
import org.yanmark.markoni.domain.models.bindings.products.ProductCreateBindingModel;
import org.yanmark.markoni.domain.models.services.CategoryServiceModel;
import org.yanmark.markoni.domain.models.services.ProductServiceModel;
import org.yanmark.markoni.errors.ProductNotFoundException;
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
    private final CloudinaryService cloudinaryService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              CategoryService categoryService,
                              CloudinaryService cloudinaryService,
                              ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.categoryService = categoryService;
        this.cloudinaryService = cloudinaryService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductServiceModel saveProduct(ProductServiceModel productService,
                                           ProductCreateBindingModel productCreate) throws IOException {
        Product checkProduct = this.productRepository.findByName(productCreate.getName())
                .orElse(null);
        if (checkProduct != null) {
            throw new ProductNotFoundException("Product with this name already exist");
        }
        Set<CategoryServiceModel> categoriesServiceModels = this.categoryService.getAllCategories().stream()
                .filter(category -> productCreate.getCategories().contains(category.getId()))
                .collect(Collectors.toSet());
        productService.setCategories(categoriesServiceModels);
        productService.setImage(this.cloudinaryService.uploadImage(productCreate.getImage()));
        Product product = this.modelMapper.map(productService, Product.class);
        try {
            product = this.productRepository.saveAndFlush(product);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return this.modelMapper.map(product, ProductServiceModel.class);
    }

    @Override
    public ProductServiceModel editProduct(ProductServiceModel productService, String id) {
        Product oldProduct = this.productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product with this id was not found!"));
        ProductServiceModel productServiceModel = this.modelMapper.map(oldProduct, ProductServiceModel.class);
        this.modelMapper.map(productService, productServiceModel);
        productServiceModel.setCategories(productService.getCategories());
        productServiceModel.setImage(oldProduct.getImage());
        productServiceModel.setId(oldProduct.getId());
        Product product = this.modelMapper.map(productServiceModel, Product.class);
        try {
            product = this.productRepository.saveAndFlush(product);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return this.modelMapper.map(product, ProductServiceModel.class);
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
        if (products.isEmpty()) {
            return new ArrayList<>();
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

    @Override
    public ProductServiceModel getProductByName(String name) {
        Product product = this.productRepository.findByName(name)
                .orElseThrow(() -> new ProductNotFoundException("Product was not found with this name!"));
        return this.modelMapper.map(product, ProductServiceModel.class);
    }

    @Override
    public ProductServiceModel getProductById(String id) {
        Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product was not found with this id!"));
        return this.modelMapper.map(product, ProductServiceModel.class);
    }

    @Override
    public List<ProductServiceModel> getAllProductsByName(String name) {
        List<Product> products = this.productRepository.findAllByName(name);
        if (products.isEmpty()) {
            return new ArrayList<>();
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
