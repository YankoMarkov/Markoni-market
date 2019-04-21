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
import org.yanmark.markoni.errors.ProductNameExistException;
import org.yanmark.markoni.errors.ProductNotFoundException;
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

    private static final String PRODUCTS_CREATE_PRODUCT = "/products/create-product";
    private static final String PRODUCTS_ALL = "/products/all";
    private static final String PRODUCTS_ALL_PRODUCTS = "/products/all-products";
    private static final String PRODUCTS_PRODUCT_DETAILS = "/products/product-details";
    private static final String PRODUCTS_EDIT_PRODUCT = "/products/edit-product";
    private static final String PRODUCTS_DETAILS = "/products/details/";
    private static final String PRODUCTS_DELETE_PRODUCT = "/products/delete-product";

    private final ProductService productService;
    private final ModelMapper modelMapper;

    @Autowired
    public ProductController(ProductService productService,
                             ModelMapper modelMapper) {
        this.productService = productService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @PageTitle("\uD835\uDCAB\uD835\uDCC7\uD835\uDC5C\uD835\uDCB9\uD835\uDCCA\uD835\uDCB8\uD835\uDCC9 \uD835\uDC9C\uD835\uDCB9\uD835\uDCB9")
    public ModelAndView add(@ModelAttribute("productCreate") ProductCreateBindingModel productCreate) {
        return this.view(PRODUCTS_CREATE_PRODUCT);
    }

    @PostMapping("/add")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ModelAndView addConfirm(@Valid @ModelAttribute("productCreate") ProductCreateBindingModel productCreate,
                                   BindingResult bindingResult) throws IOException {
        if (bindingResult.hasErrors()) {
            return view(PRODUCTS_CREATE_PRODUCT);
        }
        ProductServiceModel productServiceModel = this.modelMapper.map(productCreate, ProductServiceModel.class);
        this.productService.saveProduct(productServiceModel, productCreate);
        return this.redirect(PRODUCTS_ALL);
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @PageTitle("\uD835\uDC9C\uD835\uDCC1\uD835\uDCC1 \uD835\uDCAB\uD835\uDCC7\uD835\uDC5C\uD835\uDCB9\uD835\uDCCA\uD835\uDCB8\uD835\uDCC9\uD835\uDCC8")
    public ModelAndView all(ModelAndView modelAndView) {
        List<ProductServiceModel> productServiceModels = this.productService.getAllProducts();
        List<ProductOrderViewModel> productAllViewModels = productServiceModels.stream()
                .map(product -> this.modelMapper.map(product, ProductOrderViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("products", productAllViewModels);
        return this.view(PRODUCTS_ALL_PRODUCTS, modelAndView);
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
        return this.view(PRODUCTS_PRODUCT_DETAILS, modelAndView);
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
        return this.view(PRODUCTS_EDIT_PRODUCT, modelAndView);
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ModelAndView editConfirm(@PathVariable String id,
                                    @Valid @ModelAttribute("productEdit") ProductEditBindingModel productEdit,
                                    BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return this.view(PRODUCTS_EDIT_PRODUCT);
        }
        ProductServiceModel productServiceModel = this.modelMapper.map(productEdit, ProductServiceModel.class);
        this.productService.editProduct(productServiceModel, id);
        return this.redirect(PRODUCTS_DETAILS + id);
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
        return this.view(PRODUCTS_DELETE_PRODUCT, modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ModelAndView deleteConfirm(@PathVariable String id) {
        this.productService.deleteProduct(id);
        return this.redirect(PRODUCTS_ALL);
    }

    @ExceptionHandler({ProductNameExistException.class})
    public ModelAndView handleProductNameExistException(ProductNameExistException e) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("statusCode", e.getStatusCode());
        return modelAndView;
    }

    @ExceptionHandler({ProductNotFoundException.class})
    public ModelAndView handleProductNotFoundException(ProductNotFoundException e) {
        ModelAndView modelAndView = new ModelAndView("error");
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("statusCode", e.getStatusCode());
        return modelAndView;
    }
}
