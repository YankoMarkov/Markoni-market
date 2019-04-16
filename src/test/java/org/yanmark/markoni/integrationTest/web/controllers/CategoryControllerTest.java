package org.yanmark.markoni.integrationTest.web.controllers;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.servlet.ModelAndView;
import org.yanmark.markoni.domain.entities.Category;
import org.yanmark.markoni.domain.models.views.categories.CategoryViewModel;
import org.yanmark.markoni.repositories.CategoryRepository;
import org.yanmark.markoni.services.CategoryService;
import org.yanmark.markoni.utils.TestUtils;
import org.yanmark.markoni.web.controllers.CategoryController;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@RunWith(SpringRunner.class)
public class CategoryControllerTest {

    @Autowired
    private CategoryController categoryController;

    @MockBean
    private CategoryRepository categoryRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private CategoryService categoryService;

    private ModelAndView modelAndView;

    @Before
    public void init() {
        modelAndView = new ModelAndView();
    }

    @Test
    public void all_whenHasCategories_returnAllCategories() {
        List<Category> testCategories = TestUtils.getTestCategories(2);
        when(categoryRepository.findAllOrderByName())
                .thenReturn(testCategories);

        ModelAndView result = categoryController.all(modelAndView);

        List<CategoryViewModel> categoryViewModels = (List<CategoryViewModel>) result.getModel().get(testCategories);

        assertEquals(testCategories.size(), categoryViewModels.size());
    }
}
