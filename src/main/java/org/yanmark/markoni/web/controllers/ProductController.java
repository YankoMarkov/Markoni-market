package org.yanmark.markoni.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.yanmark.markoni.domain.models.bindings.products.ProductCreateBindingModel;
import org.yanmark.markoni.domain.models.bindings.products.ProductEditBindingModel;
import org.yanmark.markoni.domain.models.services.CategoryServiceModel;
import org.yanmark.markoni.domain.models.services.ProductServiceModel;
import org.yanmark.markoni.domain.models.services.UserServiceModel;
import org.yanmark.markoni.domain.models.views.products.ProductBuyViewModel;
import org.yanmark.markoni.domain.models.views.products.ProductDetailsViewModel;
import org.yanmark.markoni.domain.models.views.products.ProductEditViewModel;
import org.yanmark.markoni.services.ProductService;
import org.yanmark.markoni.services.UserService;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
public class ProductController extends BaseController {

    private final ProductService productService;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductController(ProductService productService,
                             UserService userService,
                             ModelMapper modelMapper) {
        this.productService = productService;
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ModelAndView add(@ModelAttribute("productCreate") ProductCreateBindingModel productCreate) {
        return this.view("/products/create-product");
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ModelAndView addConfirm(@Valid @ModelAttribute("productCreate") ProductCreateBindingModel productCreate,
                                   BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            return view("/products/create-product");
        }
        ProductServiceModel productServiceModel = this.modelMapper.map(productCreate, ProductServiceModel.class);
        this.productService.saveProduct(productServiceModel, productCreate);
        return this.redirect("/products/all");
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ModelAndView all(ModelAndView modelAndView) {
        List<ProductServiceModel> productServiceModels = this.productService.getAllProducts();
        List<ProductBuyViewModel> productAllViewModels = productServiceModels.stream()
                .map(product -> this.modelMapper.map(product, ProductBuyViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("products", productAllViewModels);
        return this.view("/products/all-products", modelAndView);
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView details(@PathVariable String id, ModelAndView modelAndView) {
        ProductServiceModel productServiceModel = this.productService.getProductById(id);
        ProductDetailsViewModel productDetailsViewModel = this.modelMapper.map(productServiceModel, ProductDetailsViewModel.class);
        productDetailsViewModel.setCategories(takeCategories(productServiceModel));
        modelAndView.addObject("product", productDetailsViewModel);
        return this.view("/products/product-details", modelAndView);
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ModelAndView edit(@PathVariable String id, ModelAndView modelAndView) {
        ProductServiceModel productServiceModel = this.productService.getProductById(id);
        ProductEditViewModel productEditViewModel = this.modelMapper.map(productServiceModel, ProductEditViewModel.class);
        productEditViewModel.setCategories(takeCategories(productServiceModel));
        modelAndView.addObject("product", productEditViewModel);
        return this.view("/products/edit-product", modelAndView);
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ModelAndView editConfirm(@PathVariable String id,
                                    @Valid @ModelAttribute("productEdit") ProductEditBindingModel productEdit,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return this.view("/products/edit-product");
        }
        ProductServiceModel productServiceModel = productService.getProductById(id);
        this.modelMapper.map(productEdit, productServiceModel);
        this.productService.editProduct(productServiceModel);
        return this.redirect("/products/details/" + id);
    }

    @GetMapping("/buy/{id}")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView buy(@PathVariable String id, ModelAndView modelAndView) {
        ProductServiceModel productServiceModel = this.productService.getProductById(id);
        ProductBuyViewModel productBuyViewModel = this.modelMapper.map(productServiceModel, ProductBuyViewModel.class);
        modelAndView.addObject("product", productBuyViewModel);
        return this.view("/products/buy-product", modelAndView);
    }

    @PostMapping("/buy")
    @PreAuthorize("isAuthenticated()")
    public ModelAndView buyConfirm(@RequestParam String productId, Principal principal) {
        ProductServiceModel productServiceModel = this.productService.getProductById(productId);
        UserServiceModel userServiceModel = this.userService.getUserByUsername(principal.getName());
        this.userService.userBuyProduct(productServiceModel, userServiceModel);
        return this.redirect("/users/storage");
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ModelAndView delete(@PathVariable String id, ModelAndView modelAndView) {
        ProductServiceModel productServiceModel = this.productService.getProductById(id);
        ProductEditViewModel productEditViewModel = this.modelMapper.map(productServiceModel, ProductEditViewModel.class);
        productEditViewModel.setCategories(takeCategories(productServiceModel));
        modelAndView.addObject("product", productEditViewModel);
        return this.view("/products/delete-product", modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ModelAndView deleteConfirm(@PathVariable String id) {
        this.productService.deleteProduct(id);
        return this.redirect("/products/all");
    }

    private List<String> takeCategories(ProductServiceModel productServiceModel) {
        return productServiceModel.getCategories().stream()
                .map(CategoryServiceModel::getName)
                .collect(Collectors.toList());
    }
}
