package org.yanmark.markoni.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yanmark.markoni.domain.entities.Product;
import org.yanmark.markoni.domain.models.services.ProductServiceModel;
import org.yanmark.markoni.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ProductServiceModel saveProduct(ProductServiceModel productService) {
        Product product = this.modelMapper.map(productService, Product.class);
        try {
            product = this.productRepository.saveAndFlush(product);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return this.modelMapper.map(product, ProductServiceModel.class);
    }

    @Override
    public List<ProductServiceModel> getAllProducts() {
        List<Product> products = this.productRepository.findAllOrdered();
        if (products == null) {
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
                .orElseThrow(() -> new IllegalArgumentException("Product not found!"));
        return this.modelMapper.map(product, ProductServiceModel.class);
    }

    @Override
    public ProductServiceModel getProductById(String id) {
        Product product = this.productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found!"));
        return this.modelMapper.map(product, ProductServiceModel.class);
    }

    @Override
    public List<ProductServiceModel> getAllProductsByName(String name) {
        List<Product> products = this.productRepository.findAllByName(name);
        if (products == null) {
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
