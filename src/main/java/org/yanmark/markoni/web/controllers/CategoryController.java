package org.yanmark.markoni.web.controllers;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.yanmark.markoni.domain.models.bindings.categories.CategoryCreateBindingModel;
import org.yanmark.markoni.domain.models.services.CategoryServiceModel;
import org.yanmark.markoni.domain.models.views.categories.CategoryViewModel;
import org.yanmark.markoni.services.CategoryService;
import org.yanmark.markoni.web.annotations.PageTitle;

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
    @PageTitle("\uD835\uDC9E\uD835\uDCB6\uD835\uDCC9\uD835\uDC52\uD835\uDC54\uD835\uDC5C\uD835\uDCC7\uD835\uDCCE \uD835\uDC9C\uD835\uDCB9\uD835\uDCB9")
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
        return this.redirect("/categories/all");
    }

    @GetMapping("/all")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @PageTitle("\uD835\uDC9E\uD835\uDCB6\uD835\uDCC9\uD835\uDC52\uD835\uDC54\uD835\uDC5C\uD835\uDCC7\uD835\uDCCE \uD835\uDC9C\uD835\uDCC1\uD835\uDCC1")
    public ModelAndView all(ModelAndView modelAndView) {
        List<CategoryViewModel> categoryViewModels = this.categoryService.getAllCategories().stream()
                .map(category -> this.modelMapper.map(category, CategoryViewModel.class))
                .collect(Collectors.toList());
        modelAndView.addObject("categories", categoryViewModels);
        return this.view("/categories/all-categories", modelAndView);
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @PageTitle("\uD835\uDC9E\uD835\uDCB6\uD835\uDCC9\uD835\uDC52\uD835\uDC54\uD835\uDC5C\uD835\uDCC7\uD835\uDCCE \uD835\uDC38\uD835\uDCB9\uD835\uDCBE\uD835\uDCC9")
    public ModelAndView edit(@PathVariable String id,
                             @ModelAttribute("categoryCreate") CategoryCreateBindingModel categoryCreate,
                             ModelAndView modelAndView) {
        CategoryServiceModel categoryServiceModel = this.categoryService.getCategoryById(id);
        CategoryViewModel categoryViewModel = this.modelMapper.map(categoryServiceModel, CategoryViewModel.class);
        modelAndView.addObject("category", categoryViewModel);
        return this.view("/categories/edit-category", modelAndView);
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ModelAndView editConfirm(@Valid @ModelAttribute("categoryCreate") CategoryCreateBindingModel categoryCreate,
                                    @PathVariable String id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return this.view("/categories/edit-category");
        }
        CategoryServiceModel categoryServiceModel = this.categoryService.getCategoryById(id);
        this.categoryService.editCategory(categoryServiceModel, categoryCreate);
        return this.redirect("/categories/all");
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @PageTitle("\uD835\uDC9E\uD835\uDCB6\uD835\uDCC9\uD835\uDC52\uD835\uDC54\uD835\uDC5C\uD835\uDCC7\uD835\uDCCE \uD835\uDC9F\uD835\uDC52\uD835\uDCC1\uD835\uDC52\uD835\uDCC9\uD835\uDC52")
    public ModelAndView delete(@PathVariable String id, ModelAndView modelAndView) {
        CategoryServiceModel categoryServiceModel = this.categoryService.getCategoryById(id);
        CategoryViewModel categoryViewModel = this.modelMapper.map(categoryServiceModel, CategoryViewModel.class);
        modelAndView.addObject("category", categoryViewModel);
        return this.view("/categories/delete-category", modelAndView);
    }

    @PostMapping("/delete/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    public ModelAndView deleteConfirm(@PathVariable String id) {
        this.categoryService.deleteCategory(id);
        return this.redirect("/categories/all");
    }

    @GetMapping("/fetch")
    @PreAuthorize("hasAnyAuthority('ADMIN','MODERATOR')")
    @ResponseBody
    public List<CategoryViewModel> fetch() {
        return this.categoryService.getAllCategories().stream()
                .map(category -> this.modelMapper.map(category, CategoryViewModel.class))
                .collect(Collectors.toList());
    }
}
