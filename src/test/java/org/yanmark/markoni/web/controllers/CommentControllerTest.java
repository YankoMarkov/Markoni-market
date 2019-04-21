package org.yanmark.markoni.web.controllers;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc(secure = false)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class CommentControllerTest {

    private static final String PRODUCTS_DETAILS = "/products/details/";
    private static final String COMMENTS_ALL_COMMENTS = "/comments/all-comments";
    private static final String COMMENTS_EDIT_COMMENT = "/comments/edit-comment";
    private static final String COMMENTS_ALL = "/comments/all";
    private static final String COMMENTS_DELETE_COMMENT = "/comments/delete-comment";

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testAdd_addComment_commentAdded() {
    }
}
