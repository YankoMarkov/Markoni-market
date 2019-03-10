package markoni.services;

import markoni.models.services.CategoryServiceModel;

import java.util.List;

public interface CategoryService {

    CategoryServiceModel saveCategory(CategoryServiceModel categoryService);

    CategoryServiceModel getCategoryById(String id);

    CategoryServiceModel getCategoryByName(String name);

    List<CategoryServiceModel> getAllCategories();

    boolean categoryExist(String name);
}
