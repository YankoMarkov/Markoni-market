package markoni.web.controllers;

import markoni.domain.models.bindings.ProductCreateBindingModel;
import markoni.domain.models.services.CategoryServiceModel;
import markoni.domain.models.services.ProductServiceModel;
import markoni.domain.models.services.UserServiceModel;
import markoni.domain.models.views.CategoryViewModel;
import markoni.domain.models.views.ProductBuyViewModel;
import markoni.domain.models.views.ProductDetailsViewModel;
import markoni.domain.models.views.ProductHomeViewModel;
import markoni.services.CategoryService;
import markoni.services.ProductService;
import markoni.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/product")
public class ProductController extends BaseController {
	
	private final ProductService productService;
	private final CategoryService categoryService;
	private final UserService userService;
	private final ModelMapper modelMapper;
	
	@Autowired
	public ProductController(ProductService productService, CategoryService categoryService, UserService userService, ModelMapper modelMapper) {
		this.productService = productService;
		this.categoryService = categoryService;
		this.userService = userService;
		this.modelMapper = modelMapper;
	}
	
	@GetMapping("/create")
	public ModelAndView productCreate(@ModelAttribute("productCreate") ProductCreateBindingModel productCreate,
	                                  ModelAndView modelAndView) {
		List<CategoryViewModel> categoryViewModels = this.categoryService.getAllCategories().stream()
				.map(category -> this.modelMapper.map(category, CategoryViewModel.class))
				.collect(Collectors.toList());
		modelAndView.addObject("categories", categoryViewModels);
		return this.view("productCreate", modelAndView);
	}
	
	@PostMapping("/create")
	public ModelAndView productCreateConfirm(@Valid @ModelAttribute("productCreate") ProductCreateBindingModel productCreate,
	                                         BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return this.view("productCreate");
		}
		ProductServiceModel productServiceModel = this.modelMapper.map(productCreate, ProductServiceModel.class);
		CategoryServiceModel categoryServiceModel = this.categoryService.getCategoryById(productCreate.getCategory());
		productServiceModel.setCategory(categoryServiceModel);
		categoryServiceModel.getProducts().add(productServiceModel);
		
		if (this.productService.saveProduct(productServiceModel) == null) {
			return this.view("productCreate");
		}
		return this.redirect("/home");
	}
	
	@GetMapping("/details")
	public ModelAndView productDetails(@RequestParam("id") String id, ModelAndView modelAndView) {
		ProductServiceModel productServiceModel = this.productService.getProductById(id);
		ProductDetailsViewModel productDetailsViewModel = this.modelMapper.map(productServiceModel, ProductDetailsViewModel.class);
		productDetailsViewModel.setCategory(productServiceModel.getCategory().getName());
		modelAndView.addObject("product", productDetailsViewModel);
		return this.view("productDetails", modelAndView);
	}
	
	@GetMapping("/buy")
	public ModelAndView productBuy(@RequestParam("id") String id, ModelAndView modelAndView) {
		ProductServiceModel productServiceModel = this.productService.getProductById(id);
		ProductBuyViewModel productBuyViewModel = this.modelMapper.map(productServiceModel, ProductBuyViewModel.class);
		modelAndView.addObject("product", productBuyViewModel);
		return this.view("productBuy", modelAndView);
	}
	
	@PostMapping("/buy")
	public ModelAndView productBuyConfirm(@RequestParam String productId, HttpSession session) {
		ProductServiceModel productServiceModel = this.productService.getProductById(productId);
		UserServiceModel userServiceModel = this.userService.getUserByUsername(session.getAttribute("username").toString());
		userServiceModel.getProducts().add(productServiceModel);
		this.userService.saveUser(userServiceModel);
		return this.redirect("/user/account");
	}
	
	@GetMapping("/delete")
	public ModelAndView productDelete() {
		return null;
	}
	
	@GetMapping("/searched")
	public ModelAndView search(HttpSession session,
	                           ModelAndView modelAndView,
	                           @RequestParam(required = false) String searchName) {
		if (session.getAttribute("username") != null) {
			List<ProductHomeViewModel> productHomeViewModels = this.productService.getAllProductsByName(searchName).stream()
					.map(product -> this.modelMapper.map(product, ProductHomeViewModel.class))
					.collect(Collectors.toList());
			modelAndView.addObject("products", productHomeViewModels);
			return this.view("productSearch", modelAndView);
		}
		return redirect("/home");
	}
}
