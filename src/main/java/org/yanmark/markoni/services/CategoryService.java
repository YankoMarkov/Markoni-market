package org.yanmark.markoni.services;

import org.yanmark.markoni.domain.models.bindings.categories.CategoryCreateBindingModel;
import org.yanmark.markoni.domain.models.services.CategoryServiceModel;

import java.util.List;

public interface CategoryService {

    CategoryServiceModel saveCategory(CategoryServiceModel categoryService);

    CategoryServiceModel editCategory(CategoryServiceModel categoryService, CategoryCreateBindingModel categoryCreate);

    void deleteCategory(String id);

    CategoryServiceModel getCategoryById(String id);

    CategoryServiceModel getCategoryByName(String name);

    List<CategoryServiceModel> getAllCategories();
}
