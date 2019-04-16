package org.yanmark.markoni.unitTest.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.yanmark.markoni.domain.entities.Category;
import org.yanmark.markoni.domain.models.services.CategoryServiceModel;
import org.yanmark.markoni.repositories.CategoryRepository;
import org.yanmark.markoni.services.CategoryService;
import org.yanmark.markoni.services.CategoryServiceImpl;
import org.yanmark.markoni.utils.TestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CategoryServiceTest {

    @Mock
    private CategoryRepository mockCategoryRepository;

    private CategoryService categoryService;

    private ModelMapper modelMapper;

    @Before
    public void init() {
        modelMapper = new ModelMapper();
        categoryService = new CategoryServiceImpl(mockCategoryRepository, modelMapper);
    }

    @Test
    public void saveCategory_whenValidCategory_returnPersistedCategory() {
        Category testCategory = TestUtils.getTestCategory();
        when(mockCategoryRepository.saveAndFlush(Mockito.any(Category.class)))
                .thenReturn(testCategory);

        CategoryServiceModel categoryServiceModel = modelMapper.map(testCategory, CategoryServiceModel.class);
        CategoryServiceModel persistedModel = categoryService.saveCategory(categoryServiceModel);

        assertEquals(categoryServiceModel.getId(), persistedModel.getId());
    }

    @Test(expected = Exception.class)
    public void saveCategory_whenCategoryIsNull_throwException() {
        categoryService.saveCategory(null);
        verify(mockCategoryRepository).saveAndFlush(any());
    }

    @Test(expected = Exception.class)
    public void saveCategory_whenCategoryExist_throwException() {
        Category testCategory = TestUtils.getTestCategory();
        when(mockCategoryRepository.findByName(Mockito.anyString()))
                .thenReturn(Optional.of(testCategory));

        CategoryServiceModel categoryServiceModel = modelMapper.map(testCategory, CategoryServiceModel.class);
        categoryService.saveCategory(categoryServiceModel);

        verify(mockCategoryRepository).saveAndFlush(any());
    }

    @Test
    public void editCategory_whenEditCategory_returnEditedCategory() {
        Category beforeEditCategory = TestUtils.getTestCategory();
        Category editCategory = new Category() {{
            setName("testCategory");
        }};
        String categoryId = beforeEditCategory.getId();
        when(mockCategoryRepository.findById(categoryId))
                .thenReturn(Optional.of(beforeEditCategory));
        when(mockCategoryRepository.saveAndFlush(Mockito.any(Category.class)))
                .thenReturn(editCategory);
        CategoryServiceModel categoryServiceModel = modelMapper.map(editCategory, CategoryServiceModel.class);

        CategoryServiceModel editCategoryServiceModel = categoryService.editCategory(categoryServiceModel, categoryId);

        assertEquals("testCategory", editCategoryServiceModel.getName());
    }

    @Test
    public void deleteCategory_whenDeleteCategory_void() {
        Category testCategory = TestUtils.getTestCategory();

        String categoryId = testCategory.getId();
        categoryService.deleteCategory(categoryId);

        verify(mockCategoryRepository).deleteById(categoryId);
    }

    @Test
    public void getCategoryById_whenFindCategoryById_returnCategory() {
        Category testCategory = TestUtils.getTestCategory();
        String categoryId = testCategory.getId();
        when(mockCategoryRepository.findById(categoryId))
                .thenReturn(Optional.of(testCategory));

        CategoryServiceModel categoryServiceModel = categoryService.getCategoryById(categoryId);

        assertNotNull(testCategory);
        assertEquals(categoryId, categoryServiceModel.getId());
        assertEquals(testCategory.getName(), categoryServiceModel.getName());
    }

    @Test(expected = Exception.class)
    public void getCategoryById_whenNoFindCategoryById_throwException() {
        Category testCategory = TestUtils.getTestCategory();
        String categoryId = testCategory.getId();

        categoryService.getCategoryById(categoryId);

        verify(mockCategoryRepository).findByName(anyString());
    }

    @Test
    public void getCategoryByName_whenFindCategoryByName_returnCategory() {
        Category testCategory = TestUtils.getTestCategory();
        String categoryName = testCategory.getName();
        when(mockCategoryRepository.findByName(categoryName))
                .thenReturn(Optional.of(testCategory));

        CategoryServiceModel categoryServiceModel = categoryService.getCategoryByName(categoryName);

        assertNotNull(testCategory);
        assertEquals(categoryName, categoryServiceModel.getName());
        assertEquals(testCategory.getId(), categoryServiceModel.getId());
    }

    @Test(expected = Exception.class)
    public void getCategoryByName_whenNoFindCategoryByName_returnCategory() {
        Category testCategory = TestUtils.getTestCategory();
        String categoryName = testCategory.getName();

        categoryService.getCategoryByName(categoryName);

        verify(mockCategoryRepository).findByName(anyString());
    }

    @Test
    public void getAllCategories_when2Categories_return2Categories() {
        when(mockCategoryRepository.findAllOrderByName())
                .thenReturn(TestUtils.getTestCategories(2));

        List<CategoryServiceModel> result = categoryService.getAllCategories();

        assertEquals(2, result.size());
    }

    @Test
    public void getAllCategories_whenNoCategories_returnEmptyCategories() {
        when(mockCategoryRepository.findAllOrderByName())
                .thenReturn(new ArrayList<>());

        List<CategoryServiceModel> result = categoryService.getAllCategories();

        assertEquals(0, result.size());
    }
}
