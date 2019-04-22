package org.yanmark.markoni.web.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.yanmark.markoni.domain.models.bindings.categories.CategoryCreateBindingModel;
import org.yanmark.markoni.repositories.CategoryRepository;
import org.yanmark.markoni.utils.TestUtils;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testCreate_addCategory_returnAddCategoryPage() throws Exception {
        mockMvc.perform(get("/categories/add"))
                .andExpect(view().name("/categories/create-category"))
                .andExpect(model().attributeExists("categoryCreate"));
    }

    @Test
    @WithMockUser(authorities = {"ADMIN"})
    public void testCreate_addCategory_CategoryAdded() throws Exception {
        mockMvc.perform(post("/categories/add")
                .param("name", "testCategory"));

        assertEquals(1, categoryRepository.count());
    }
}
