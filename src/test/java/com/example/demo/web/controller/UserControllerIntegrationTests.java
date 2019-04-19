package com.example.demo.web.controller;


import com.example.demo.domain.entities.User;
import com.example.demo.domain.model.AuthenticationRequestModel;
import com.example.demo.domain.model.UserRegisterRequestModel;
import com.example.demo.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class UserControllerIntegrationTests
{

    private static final String INVALID_PASSWORD = "invalid_password";
    private static final String INVALID_USERNAME = "invalid_username";
    private final String USERNAME = "admin";
    private final String PASSWORD = "pass";
    private final String CONFIRM_PASSWORD = "pass";
    private final String EMAIL = "example@email.com";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Before
    public void setUp() {
       userRepository.deleteAll();
    }

    private UserRegisterRequestModel createUserRegisterModel(){
        UserRegisterRequestModel user = new UserRegisterRequestModel();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        user.setConfirmPassword(CONFIRM_PASSWORD);
        user.setEmail(EMAIL);
        return user;
    }

    private AuthenticationRequestModel createUserLoginModel(){
        AuthenticationRequestModel user = new AuthenticationRequestModel();
        user.setUsername(USERNAME);
        user.setPassword(PASSWORD);
        return user;
    }

    private User createUser(){
        User user = new User();
        user.setUsername(USERNAME);
        user.setPassword(this.passwordEncoder.encode(PASSWORD));
        user.setEmail(EMAIL);
        return user;
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //register
    @Test
    @WithAnonymousUser()
    public void registerUser_withAnonymousUser_mustReturnStatus200AndCorrectResponse() throws Exception {
        UserRegisterRequestModel user = createUserRegisterModel();
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user)))
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username")
                        .value(USERNAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email")
                        .value(EMAIL));
    }

    @Test
    @WithAnonymousUser()
    public void registerUser_withAnonymousUser_mustCreateUserInDB() throws Exception {
        UserRegisterRequestModel user = createUserRegisterModel();
        Assert.assertEquals(0, userRepository.count());
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user)));
        Assert.assertEquals(1, userRepository.count());
    }

    @Test
    @WithMockUser()
    public void registerUser_withLoggedInUser_mustReturnStatus403() throws Exception {
        UserRegisterRequestModel user = createUserRegisterModel();
        mockMvc.perform(post("/users/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(user)))
                .andExpect(status().is(403));

    }

    //login
    @Test
    @WithAnonymousUser()
    public void loginUser_withAnonymousUser_mustReturnStatus200AndCorrectResponse() throws Exception {
        this.userRepository.save(createUser());
        AuthenticationRequestModel loginModel = createUserLoginModel();
        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(loginModel)))
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username")
                        .value(USERNAME))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token")
                        .exists());
    }

    @Test
    @WithMockUser()
    public void loginUser_withLoggedInUser_mustReturnStatus403() throws Exception {
        this.userRepository.save(createUser());
        AuthenticationRequestModel loginModel = createUserLoginModel();
        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(loginModel)))
                .andExpect(status().is(403));
    }

    @Test
    @WithAnonymousUser()
    public void loginUser_withInvalidUsername_mustReturnStatus404() throws Exception {
        this.userRepository.save(createUser());
        AuthenticationRequestModel loginModel = createUserLoginModel();
        loginModel.setUsername(INVALID_USERNAME);
        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(loginModel)))
                .andExpect(status().is(404));
    }


    @Test
    @WithAnonymousUser()
    public void loginUser_withInvalidPassword_mustReturnStatus404() throws Exception {
        this.userRepository.save(createUser());
        AuthenticationRequestModel loginModel = createUserLoginModel();
        loginModel.setPassword(INVALID_PASSWORD);
        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(loginModel)))
                .andExpect(status().is(404));
    }






}
