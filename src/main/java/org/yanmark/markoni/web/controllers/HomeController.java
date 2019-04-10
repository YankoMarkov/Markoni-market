package org.yanmark.markoni.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.yanmark.markoni.domain.models.views.categories.CategoryViewModel;
import org.yanmark.markoni.domain.models.views.products.ProductHomeViewModel;
import org.yanmark.markoni.domain.models.views.products.ProductIndexViewModel;
import org.yanmark.markoni.services.CategoryService;
import org.yanmark.markoni.services.HomeService;
import org.yanmark.markoni.services.ProductService;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController extends BaseController {

    private final CategoryService categoryService;
    private final ProductService productService;
    private final HomeService homeService;
    private final ModelMapper modelMapper;

    @Autowired
    public HomeController(CategoryService categoryService,
                          ProductService productService,
                          HomeService homeService,
                          ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.productService = productService;
        this.homeService = homeService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    @PreAuthorize("isAnonymous()")
    public ModelAndView index(ModelAndView modelAndView,
                              @RequestParam(required = false) String categoryId) {
        List<CategoryViewModel> categoryViewModels = this.categoryService.getAllCategories().stream()
                .map(category -> this.modelMapper.map(category, CategoryViewModel.class))
                .collect(Collectors.toList());

        List<ProductIndexViewModel> productIndexViewModels =
                this.homeService.takeProducts(categoryId, modelAndView).stream()
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

        List<ProductHomeViewModel> productHomeViewModels =
                this.homeService.takeProducts(categoryId, modelAndView).stream()
                        .map(product -> this.modelMapper.map(product, ProductHomeViewModel.class))
                        .collect(Collectors.toList());

        modelAndView.addObject("products", productHomeViewModels);
        modelAndView.addObject("categories", categoryViewModels);
        return view("home", modelAndView);
    }

    @GetMapping("/search")
    public ModelAndView search(ModelAndView modelAndView,
                               @RequestParam(required = false) String searchName) {
        List<CategoryViewModel> categoryViewModels = this.categoryService.getAllCategories().stream()
                .map(category -> this.modelMapper.map(category, CategoryViewModel.class))
                .collect(Collectors.toList());
        List<ProductHomeViewModel> productHomeViewModels = this.productService.getAllProductsByName(searchName).stream()
                .map(product -> this.modelMapper.map(product, ProductHomeViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("products", productHomeViewModels);
        modelAndView.addObject("categories", categoryViewModels);
        return this.view("home", modelAndView);
    }
}
