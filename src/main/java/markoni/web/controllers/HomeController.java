package markoni.web.controllers;

import markoni.models.services.CategoryServiceModel;
import markoni.models.services.ProductServiceModel;
import markoni.models.views.CategoryViewModel;
import markoni.models.views.ProductHomeViewModel;
import markoni.services.CategoryService;
import markoni.services.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
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
	public ModelAndView index() {
		return this.view("index");
	}
	
	@GetMapping("/home")
	public ModelAndView home(ModelAndView modelAndView,
	                         @RequestParam(required = false) String categoryId) {
		List<CategoryViewModel> categoryViewModels = this.categoryService.getAllCategories().stream()
				.map(category -> this.modelMapper.map(category, CategoryViewModel.class))
				.collect(Collectors.toList());
		
		Collection<ProductServiceModel> products = null;
		
		if (categoryId != null && categoryId.length() > 0) {
			CategoryServiceModel selectedCategory = this.categoryService.getCategoryById(categoryId);
			if (selectedCategory != null) {
				products = selectedCategory.getProducts();
				modelAndView.addObject("categoryName", selectedCategory.getName());
			}
		}
		if (products == null) {
			products = this.productService.getAllProducts();
		}
		List<ProductHomeViewModel> productHomeViewModels = products.stream()
				.map(product -> this.modelMapper.map(product, ProductHomeViewModel.class))
				.collect(Collectors.toList());
		
		modelAndView.addObject("products", productHomeViewModels);
		modelAndView.addObject("categories", categoryViewModels);
		return view("home", modelAndView);
	}
}
