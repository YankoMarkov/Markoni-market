package org.yanmark.markoni.web.controllers;

import org.junit.Assert;
import org.junit.Before;
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
import org.yanmark.markoni.repositories.UserRepository;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UsersControllerTests {

    @Autowired
    public MockMvc mockMvc;

    @Autowired
    public UserRepository userRepository;

    @Before
    public synchronized void setUp() {
        userRepository.deleteAll();
    }

    @Test
    public void testLogin_getLogin_returnsLoginPage() throws Exception {
        mockMvc.perform(get("/users/login"))
                .andExpect(view().name("/users/login"));
    }

    @Test
    public void testRegister_getRegister_returnsRegisterPage() throws Exception {
        mockMvc.perform(get("/users/register"))
                .andExpect(view().name("/users/register"));
    }

    @Test
    public void testRegister_postRegister_createUser() throws Exception {
        mockMvc.perform(post("/users/register")
                .param("username", "testUser")
                .param("password", "123")
                .param("confirmPassword", "123")
                .param("address", "testAddress")
                .param("email", "test@tets.t"));

        Assert.assertEquals(1, userRepository.count());
    }

    @Test
    public void testRegister_postRegister_redirect() throws Exception {
        mockMvc.perform(post("/users/register")
                .param("username", "testUser")
                .param("password", "123")
                .param("confirmPassword", "123")
                .param("address", "testAddress")
                .param("email", "test@tets.t"))
                .andExpect(view().name("redirect:/users/login"));
    }

    @Test
    @WithMockUser
    public void testProfile_GetUserProfile_returnUserProfilePage() throws Exception {
        mockMvc.perform(get("/users/profile"))
                .andExpect(view().name("/users/edit-profile"))
                .andExpect(model().attributeExists("userProfile"));
    }
}
