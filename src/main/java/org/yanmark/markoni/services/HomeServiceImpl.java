package org.yanmark.markoni.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.yanmark.markoni.domain.models.services.CategoryServiceModel;
import org.yanmark.markoni.domain.models.services.ProductServiceModel;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class HomeServiceImpl implements HomeService {

    private final ProductService productService;
    private final CategoryService categoryService;

    @Autowired
    public HomeServiceImpl(ProductService productService,
                           CategoryService categoryService) {
        this.productService = productService;
        this.categoryService = categoryService;
    }

    @Override
    public List<ProductServiceModel> takeProducts(String categoryId, ModelAndView modelAndView) {
        List<ProductServiceModel> productsService = this.productService.getAllProducts();
        if (categoryId != null && categoryId.length() > 0) {
            CategoryServiceModel selectedCategory = this.categoryService.getCategoryById(categoryId);
            if (selectedCategory != null) {
                productsService = productsService.stream()
                        .filter(product -> product.getCategories().stream()
                                .anyMatch(category -> category.getId().equals(categoryId)))
                        .collect(Collectors.toList());
                modelAndView.addObject("categoryName", selectedCategory.getName());
            }
        }
        return productsService;
    }
}
