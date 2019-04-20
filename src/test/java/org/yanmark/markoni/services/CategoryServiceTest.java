package org.yanmark.markoni.services;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.modelmapper.ModelMapper;
import org.yanmark.markoni.domain.entities.Category;
import org.yanmark.markoni.domain.models.services.CategoryServiceModel;
import org.yanmark.markoni.repositories.CategoryRepository;
import org.yanmark.markoni.utils.TestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
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
        when(mockCategoryRepository.saveAndFlush(any(Category.class)))
                .thenReturn(testCategory);

        CategoryServiceModel givenServiceModel = modelMapper.map(testCategory, CategoryServiceModel.class);
        CategoryServiceModel returnServiceModel = categoryService.saveCategory(givenServiceModel);

        assertEquals(givenServiceModel.getId(), returnServiceModel.getId());
    }

    @Test(expected = Exception.class)
    public void saveCategory_whenCategoryIsNull_throwException() {
        categoryService.saveCategory(null);
        verify(mockCategoryRepository).saveAndFlush(any());
    }

    @Test(expected = Exception.class)
    public void saveCategory_whenCategoryExist_throwException() {
        Category testCategory = TestUtils.getTestCategory();
        when(mockCategoryRepository.findByName(anyString()))
                .thenReturn(Optional.of(testCategory));

        CategoryServiceModel categoryServiceModel = modelMapper.map(testCategory, CategoryServiceModel.class);
        categoryService.saveCategory(categoryServiceModel);

        verify(mockCategoryRepository).saveAndFlush(any());
    }

    @Test
    public void editCategory_whenEditCategory_returnEditedCategory() {
        Category testCategory = TestUtils.getTestCategory();
        Category editCategory = new Category() {{
            setName("testCategory");
        }};
        when(mockCategoryRepository.findById(anyString()))
                .thenReturn(Optional.of(testCategory));
        when(mockCategoryRepository.saveAndFlush(any()))
                .thenReturn(editCategory);
        CategoryServiceModel givenServiceModel = modelMapper.map(testCategory, CategoryServiceModel.class);
        CategoryServiceModel returnServiceModel = categoryService.editCategory(givenServiceModel, testCategory.getId());

        assertEquals("testCategory", returnServiceModel.getName());
    }

    @Test
    public void deleteCategory_whenDeleteCategory_void() {
        Category testCategory = TestUtils.getTestCategory();

        categoryService.deleteCategory(testCategory.getId());

        verify(mockCategoryRepository).deleteById(testCategory.getId());
    }

    @Test
    public void getCategoryById_whenFindCategoryById_returnCategory() {
        Category testCategory = TestUtils.getTestCategory();
        when(mockCategoryRepository.findById(anyString()))
                .thenReturn(Optional.of(testCategory));

        CategoryServiceModel categoryServiceModel = categoryService.getCategoryById(testCategory.getId());

        assertEquals(testCategory.getId(), categoryServiceModel.getId());
        assertEquals(testCategory.getName(), categoryServiceModel.getName());
    }

    @Test(expected = Exception.class)
    public void getCategoryById_whenNoFindCategoryById_throwException() {
        Category testCategory = TestUtils.getTestCategory();

        categoryService.getCategoryById(testCategory.getId());

        verify(mockCategoryRepository).findByName(anyString());
    }

    @Test
    public void getCategoryByName_whenFindCategoryByName_returnCategory() {
        Category testCategory = TestUtils.getTestCategory();
        when(mockCategoryRepository.findByName(anyString()))
                .thenReturn(Optional.of(testCategory));

        CategoryServiceModel categoryServiceModel = categoryService.getCategoryByName(testCategory.getName());

        assertEquals(testCategory.getName(), categoryServiceModel.getName());
        assertEquals(testCategory.getId(), categoryServiceModel.getId());
    }

    @Test(expected = Exception.class)
    public void getCategoryByName_whenNoFindCategoryByName_returnCategory() {
        Category testCategory = TestUtils.getTestCategory();

        categoryService.getCategoryByName(testCategory.getName());

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
