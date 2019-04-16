package org.yanmark.markoni.services;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yanmark.markoni.domain.entities.Category;
import org.yanmark.markoni.domain.models.services.CategoryServiceModel;
import org.yanmark.markoni.errors.CategoryNameExistException;
import org.yanmark.markoni.errors.CategoryNotFoundException;
import org.yanmark.markoni.repositories.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, ModelMapper modelMapper) {
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public CategoryServiceModel saveCategory(CategoryServiceModel categoryService) {
        Category checkCategory = this.categoryRepository.findByName(categoryService.getName())
                .orElse(null);
        if (checkCategory != null) {
            throw new CategoryNameExistException("Category with this name already exist!");
        }
        Category category = this.modelMapper.map(categoryService, Category.class);
        try {
            category = this.categoryRepository.saveAndFlush(category);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
        return this.modelMapper.map(category, CategoryServiceModel.class);
    }

    @Override
    public CategoryServiceModel editCategory(CategoryServiceModel categoryService, String id) {
        Category category = this.categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category with this id was not found!"));
        CategoryServiceModel categoryServiceModel = this.modelMapper.map(category, CategoryServiceModel.class);
        categoryServiceModel.setName(categoryService.getName());
        return saveCategory(categoryServiceModel);
    }

    @Override
    public void deleteCategory(String id) {
        try {
            this.categoryRepository.deleteById(id);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

    @Override
    public CategoryServiceModel getCategoryById(String id) {
        Category category = this.categoryRepository.findById(id)
                .orElseThrow(() -> new CategoryNotFoundException("Category was not found with this id!"));
        return this.modelMapper.map(category, CategoryServiceModel.class);
    }

    @Override
    public CategoryServiceModel getCategoryByName(String name) {
        Category category = this.categoryRepository.findByName(name)
                .orElseThrow(() -> new CategoryNotFoundException("Category was not found with this name!"));
        return this.modelMapper.map(category, CategoryServiceModel.class);
    }

    @Override
    public List<CategoryServiceModel> getAllCategories() {
        return this.categoryRepository.findAllOrderByName().stream()
                .map(category -> this.modelMapper.map(category, CategoryServiceModel.class))
                .collect(Collectors.toUnmodifiableList());
    }
}
