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
import org.yanmark.markoni.domain.models.views.products.ProductDetailsViewModel;
import org.yanmark.markoni.domain.models.views.products.ProductEditViewModel;
import org.yanmark.markoni.domain.models.views.products.ProductOrderViewModel;
import org.yanmark.markoni.services.ProductService;
import org.yanmark.markoni.services.UserService;
import org.yanmark.markoni.web.annotations.PageTitle;

import javax.validation.Valid;
import java.io.IOException;
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
    @PageTitle("\uD835\uDCAB\uD835\uDCC7\uD835\uDC5C\uD835\uDCB9\uD835\uDCCA\uD835\uDCB8\uD835\uDCC9 \uD835\uDC9C\uD835\uDCB9\uD835\uDCB9")
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
    @PageTitle("\uD835\uDCAB\uD835\uDCC7\uD835\uDC5C\uD835\uDCB9\uD835\uDCCA\uD835\uDCB8\uD835\uDCC9 \uD835\uDC9C\uD835\uDCC1\uD835\uDCC1")
    public ModelAndView all(ModelAndView modelAndView) {
        List<ProductServiceModel> productServiceModels = this.productService.getAllProducts();
        List<ProductOrderViewModel> productAllViewModels = productServiceModels.stream()
                .map(product -> this.modelMapper.map(product, ProductOrderViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("products", productAllViewModels);
        return this.view("/products/all-products", modelAndView);
    }

    @GetMapping("/details/{id}")
    @PreAuthorize("isAuthenticated()")
    @PageTitle("\uD835\uDCAB\uD835\uDCC7\uD835\uDC5C\uD835\uDCB9\uD835\uDCCA\uD835\uDCB8\uD835\uDCC9 \uD835\uDC9F\uD835\uDC52\uD835\uDCC9\uD835\uDCB6\uD835\uDCBE\uD835\uDCC1\uD835\uDCC8")
    public ModelAndView details(@PathVariable String id, ModelAndView modelAndView) {
        ProductServiceModel productServiceModel = this.productService.getProductById(id);
        ProductDetailsViewModel productDetailsViewModel = this.modelMapper.map(productServiceModel, ProductDetailsViewModel.class);
        List<String> categories = productServiceModel.getCategories().stream()
                .map(CategoryServiceModel::getName)
                .collect(Collectors.toList());
        productDetailsViewModel.setCategories(categories);
        modelAndView.addObject("product", productDetailsViewModel);
        return this.view("/products/product-details", modelAndView);
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @PageTitle("\uD835\uDCAB\uD835\uDCC7\uD835\uDC5C\uD835\uDCB9\uD835\uDCCA\uD835\uDCB8\uD835\uDCC9 \uD835\uDC38\uD835\uDCB9\uD835\uDCBE\uD835\uDCC9")
    public ModelAndView edit(@PathVariable String id,
                             @ModelAttribute("productEdit") ProductEditBindingModel productEdit,
                             ModelAndView modelAndView) {
        ProductServiceModel productServiceModel = this.productService.getProductById(id);
        ProductEditViewModel productEditViewModel = this.modelMapper.map(productServiceModel, ProductEditViewModel.class);
        List<String> categories = productServiceModel.getCategories().stream()
                .map(CategoryServiceModel::getName)
                .collect(Collectors.toList());
        productEditViewModel.setCategories(categories);
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

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @PageTitle("\uD835\uDCAB\uD835\uDCC7\uD835\uDC5C\uD835\uDCB9\uD835\uDCCA\uD835\uDCB8\uD835\uDCC9 \uD835\uDC9F\uD835\uDC52\uD835\uDCC1\uD835\uDC52\uD835\uDCC9\uD835\uDC52")
    public ModelAndView delete(@PathVariable String id, ModelAndView modelAndView) {
        ProductServiceModel productServiceModel = this.productService.getProductById(id);
        ProductEditViewModel productEditViewModel = this.modelMapper.map(productServiceModel, ProductEditViewModel.class);
        List<String> categories = productServiceModel.getCategories().stream()
                .map(CategoryServiceModel::getName)
                .collect(Collectors.toList());
        productEditViewModel.setCategories(categories);
        modelAndView.addObject("product", productEditViewModel);
        return this.view("/products/delete-product", modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ModelAndView deleteConfirm(@PathVariable String id) {
        this.productService.deleteProduct(id);
        return this.redirect("/products/all");
    }
}
