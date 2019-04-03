package org.yanmark.markoni.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.yanmark.markoni.domain.models.bindings.categories.CategoryCreateBindingModel;
import org.yanmark.markoni.domain.models.services.CategoryServiceModel;
import org.yanmark.markoni.domain.models.views.categories.CategoryViewModel;
import org.yanmark.markoni.services.CategoryService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/categories")
public class CategoryController extends BaseController {
	
	private final CategoryService categoryService;
	private final ModelMapper modelMapper;
	
	@Autowired
	public CategoryController(CategoryService categoryService, ModelMapper modelMapper) {
		this.categoryService = categoryService;
		this.modelMapper = modelMapper;
	}
	
	@GetMapping("/add")
	@PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
	public ModelAndView create(@ModelAttribute("categoryCreate") CategoryCreateBindingModel categoryCreate) {
		return this.view("/categories/create-category");
	}
	
	@PostMapping("/add")
	@PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
	public ModelAndView createConfirm(@Valid @ModelAttribute("categoryCreate") CategoryCreateBindingModel categoryCreate,
	                                  BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return this.view("/categories/create-category");
		}
		CategoryServiceModel categoryServiceModel = this.modelMapper.map(categoryCreate, CategoryServiceModel.class);
		this.categoryService.saveCategory(categoryServiceModel);
		return this.redirect("/home");
	}
	
	@GetMapping("/all")
	@PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
	public ModelAndView all(ModelAndView modelAndView) {
		List<CategoryViewModel> categoryViewModels = this.categoryService.getAllCategories().stream()
				.map(category -> this.modelMapper.map(category, CategoryViewModel.class))
				.collect(Collectors.toList());
		modelAndView.addObject("categories", categoryViewModels);
		return this.view("/categories/all-categories", modelAndView);
	}
}
