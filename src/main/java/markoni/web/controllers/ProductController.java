package markoni.web.controllers;

import markoni.models.bindings.ProductCreateBindingModel;
import markoni.models.services.CategoryServiceModel;
import markoni.models.services.ProductServiceModel;
import markoni.models.views.CategoryViewModel;
import markoni.models.views.ProductHomeViewModel;
import markoni.services.CategoryService;
import markoni.services.ProductService;
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
	private final ModelMapper modelMapper;
	
	@Autowired
	public ProductController(ProductService productService, CategoryService categoryService, ModelMapper modelMapper) {
		this.productService = productService;
		this.categoryService = categoryService;
		this.modelMapper = modelMapper;
	}
	
	@GetMapping("/create")
	public ModelAndView product(@ModelAttribute("productCreate") ProductCreateBindingModel productCreate,
	                            ModelAndView modelAndView) {
		List<CategoryViewModel> categoryViewModels = this.categoryService.getAllCategories().stream()
				.map(category -> this.modelMapper.map(category, CategoryViewModel.class))
				.collect(Collectors.toList());
		modelAndView.addObject("categories", categoryViewModels);
		return this.view("productCreate", modelAndView);
	}
	
	@PostMapping("/create")
	public ModelAndView productConfirm(@Valid @ModelAttribute("productCreate") ProductCreateBindingModel productCreate,
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
	public ModelAndView details() {
		return this.view("productDetails");
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
