package org.yanmark.markoni.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.yanmark.markoni.domain.models.services.CategoryServiceModel;
import org.yanmark.markoni.domain.models.services.ProductServiceModel;
import org.yanmark.markoni.domain.models.views.categories.CategoryViewModel;
import org.yanmark.markoni.domain.models.views.products.ProductHomeViewModel;
import org.yanmark.markoni.domain.models.views.products.ProductIndexViewModel;
import org.yanmark.markoni.services.CategoryService;
import org.yanmark.markoni.services.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController extends BaseController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Autowired
    public HomeController(CategoryService categoryService, ProductService productService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    @PreAuthorize("isAnonymous()")
    public ModelAndView index(ModelAndView modelAndView,
                              @RequestParam(required = false) String categoryId) {
        List<CategoryViewModel> categoryViewModels = this.categoryService.getAllCategories().stream()
                .map(category -> this.modelMapper.map(category, CategoryViewModel.class))
                .collect(Collectors.toList());

        List<ProductServiceModel> products = this.productService.getAllProducts();

        if (categoryId != null && categoryId.length() > 0) {
            CategoryServiceModel selectedCategory = this.categoryService.getCategoryById(categoryId);
            if (selectedCategory != null) {
                products = products.stream()
                        .filter(product -> product.getCategories().stream()
                                .anyMatch(category -> category.getId().equals(categoryId)))
                        .collect(Collectors.toList());
                modelAndView.addObject("categoryName", selectedCategory.getName());
            }
        }
        List<ProductIndexViewModel> productIndexViewModels = products.stream()
                .map(product -> this.modelMapper.map(product, ProductIndexViewModel.class))
                .collect(Collectors.toList());

        modelAndView.addObject("products", productIndexViewModels);
        modelAndView.addObject("categories", categoryViewModels);
        return view("index", modelAndView);
    }

    @GetMapping("/home")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView home(ModelAndView modelAndView,
                             @RequestParam(required = false) String categoryId) {
        List<CategoryViewModel> categoryViewModels = this.categoryService.getAllCategories().stream()
                .map(category -> this.modelMapper.map(category, CategoryViewModel.class))
                .collect(Collectors.toList());

        List<ProductServiceModel> products = this.productService.getAllProducts();

        if (categoryId != null && categoryId.length() > 0) {
            CategoryServiceModel selectedCategory = this.categoryService.getCategoryById(categoryId);
            if (selectedCategory != null) {
                products = products.stream()
                        .filter(product -> product.getCategories().stream()
                                .anyMatch(category -> category.getId().equals(categoryId)))
                        .collect(Collectors.toList());
                modelAndView.addObject("categoryName", selectedCategory.getName());
            }
        }
        List<ProductHomeViewModel> productHomeViewModels = products.stream()
                .map(product -> this.modelMapper.map(product, ProductHomeViewModel.class))
                .collect(Collectors.toList());

        modelAndView.addObject("products", productHomeViewModels);
        modelAndView.addObject("categories", categoryViewModels);
        return view("home", modelAndView);
    }
}
