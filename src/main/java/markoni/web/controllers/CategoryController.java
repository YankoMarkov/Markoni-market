package markoni.web.controllers;

import markoni.domain.models.bindings.CategoryCreateBindingModel;
import markoni.domain.models.services.CategoryServiceModel;
import markoni.services.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
@RequestMapping("/category")
public class CategoryController extends BaseController {
	
	private final CategoryService categoryService;
	private final ModelMapper modelMapper;
	
	@Autowired
	public CategoryController(CategoryService categoryService, ModelMapper modelMapper) {
		this.categoryService = categoryService;
		this.modelMapper = modelMapper;
	}
	
	@GetMapping("/create")
	public ModelAndView create(@ModelAttribute("categoryCreate") CategoryCreateBindingModel categoryCreate) {
		return this.view("categoryCreate");
	}
	
	@PostMapping("/create")
	public ModelAndView createConfirm(@Valid @ModelAttribute("categoryCreate") CategoryCreateBindingModel categoryCreate,
	                                  BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return this.view("categoryCreate");
		}
		CategoryServiceModel categoryServiceModel = this.modelMapper.map(categoryCreate, CategoryServiceModel.class);
		
		if (this.categoryService.saveCategory(categoryServiceModel) == null) {
			return this.view("categoryCreate");
		}
		return this.redirect("/home");
	}
}
