package org.yanmark.markoni.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.yanmark.markoni.domain.models.bindings.products.ProductCreateBindingModel;
import org.yanmark.markoni.domain.models.services.ProductServiceModel;
import org.yanmark.markoni.domain.models.services.UserServiceModel;
import org.yanmark.markoni.domain.models.views.categories.CategoryViewModel;
import org.yanmark.markoni.domain.models.views.products.ProductBuyViewModel;
import org.yanmark.markoni.services.CategoryService;
import org.yanmark.markoni.services.CloudinaryService;
import org.yanmark.markoni.services.ProductService;
import org.yanmark.markoni.services.UserService;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
public class ProductController extends BaseController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final CloudinaryService cloudinaryService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductController(ProductService productService,
                             CategoryService categoryService,
                             CloudinaryService cloudinaryService,
                             UserService userService,
                             ModelMapper modelMapper) {
        this.productService = productService;
        this.categoryService = categoryService;
        this.cloudinaryService = cloudinaryService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ModelAndView productCreate(@ModelAttribute("productCreate") ProductCreateBindingModel productCreate) {
        return this.view("/products/create-product");
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ModelAndView productCreateConfirm(@Valid @ModelAttribute("productCreate") ProductCreateBindingModel productCreate,
                                             BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            return view("/products/create-product");
        }
        ProductServiceModel productServiceModel = this.modelMapper.map(productCreate, ProductServiceModel.class);
        productServiceModel.setCategories(
                this.categoryService.getAllCategories().stream()
                        .filter(category -> productCreate.getCategories().contains(category.getId()))
                        .collect(Collectors.toSet())
        );
        productServiceModel.setImage(this.cloudinaryService.uploadImage(productCreate.getImage()));
        this.productService.saveProduct(productServiceModel);
        return this.redirect("/products/all");
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ModelAndView all(ModelAndView modelAndView) {
        List<ProductBuyViewModel> productAllViewModels = this.productService.getAllProducts().stream()
                .map(product -> this.modelMapper.map(product, ProductBuyViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("products", productAllViewModels);
        return this.view("/products/all-products", modelAndView);
    }

    @GetMapping("/details")
    public ModelAndView productDetails(@RequestParam("id") String id, ModelAndView modelAndView) {
//        ProductServiceModel productServiceModel = this.productService.getProductById(id);
//        ProductDetailsViewModel productDetailsViewModel = this.modelMapper.map(productServiceModel, ProductDetailsViewModel.class);
//        productDetailsViewModel.setCategory(productServiceModel.getCategories().getName());
//        modelAndView.addObject("product", productDetailsViewModel);
//        return this.view("/products/product-
        return null;
    }

    @GetMapping("/buy")
    public ModelAndView productBuy(@RequestParam("id") String id, ModelAndView modelAndView) {
        ProductServiceModel productServiceModel = this.productService.getProductById(id);
        ProductBuyViewModel productBuyViewModel = this.modelMapper.map(productServiceModel, ProductBuyViewModel.class);
        modelAndView.addObject("product", productBuyViewModel);
        return this.view("/products/buy-product", modelAndView);
    }

    @PostMapping("/buy")
    public ModelAndView productBuyConfirm(@RequestParam String productId, HttpSession session) {
        ProductServiceModel productServiceModel = this.productService.getProductById(productId);
        UserServiceModel userServiceModel = this.userService.getUserByUsername(session.getAttribute("username").toString());
        userServiceModel.getProducts().add(productServiceModel);
        this.userService.saveUser(userServiceModel);
        return this.redirect("/users/account");
    }

    @GetMapping("/delete")
    public ModelAndView productDelete() {
        return null;
    }

    @GetMapping("/search")
    public ModelAndView search(Principal principal, ModelAndView modelAndView,
                               @RequestParam(required = false) String searchName) {
        List<CategoryViewModel> categoryViewModels = this.categoryService.getAllCategories().stream()
                .map(category -> this.modelMapper.map(category, CategoryViewModel.class))
                .collect(Collectors.toList());
        System.out.println(principal);
        System.out.println();

            return this.view("/products/products-search", modelAndView);
    }
}
