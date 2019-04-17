package com.example.demo.web.controller;


import com.example.demo.domain.entities.Employee;
import com.example.demo.repository.EmployeeRepository;
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
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;


    @Before
    public void setUp() {
       employeeRepository.deleteAll();
    }

    private Employee createEmployee() {
        Employee employee = new Employee("ivaylo", "junior");
        return employeeRepository.save(employee);
    }

    @Test
    @WithMockUser
    public void getEmployees_withLoggedInUser_mustReturnStatus200() throws Exception {
//        Employee employee  = this.createEmployee();
        mockMvc.perform(get("/employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }

    @Test
    @WithMockUser
    public void getEmployeeById_withLoggedInUser_mustReturnStatus200() throws Exception {
        Employee employee  = this.createEmployee();
        mockMvc.perform(get("/employees/" + employee.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200));
    }

    @Test
    @WithMockUser(roles={"USER","ADMIN"})
    public void createEmployee_withLoggedInAdmin_mustReturnStatus200() throws Exception {
        mockMvc.perform(post("/employees/create")
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", "ivo")
                .param("role", "user"))
                .andExpect(status().is(200));
    }

    @Test
    @WithMockUser(roles={"USER","ADMIN"})
    public void createEmployee_withLoggedInAdmin_mustCreateEmployeeInDB() throws Exception {
        mockMvc.perform(post("/employees/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{ \"name\": \"ivo\", \"role\": \"user\" }"));

        Assert.assertEquals(1, employeeRepository.count());

    }

    @Test
    @WithMockUser()
    public void createEmployee_withLoggedInRegularUser_mustReturnStatus403() throws Exception {
        mockMvc.perform(post("/employees/create")
                .contentType(MediaType.APPLICATION_JSON)
                .param("name", "ivo")
                .param("role", "user"))
                .andExpect(status().is(403));
    }


    @Test
    @WithAnonymousUser
    public void getEmployees_withAnonymousUser_mustReturnStatus403() throws Exception {
        this.createEmployee();
        mockMvc.perform(get("/employees"))
                .andExpect(status().is(403));
    }

    @Test
    @WithAnonymousUser
    public void getEmployeeById_withAnonymousUser_mustReturnStatus403() throws Exception {
        Employee employee  = this.createEmployee();
        mockMvc.perform(get("/employees/" + employee.getId()))
                .andExpect(status().is(403));
    }

    @Test
    @WithAnonymousUser
    public void createEmployee_withAnonymousUser_mustReturnStatus403() throws Exception {
        mockMvc.perform(post("/employees/create")
                .param("name", "ivo")
                .param("role", "user"))
                .andExpect(status().is(403));
    }

    @Test
    @WithAnonymousUser
    public void editEmployee_withAnonymousUser_mustReturnStatus403() throws Exception {
        Employee employee  = this.createEmployee();
        mockMvc.perform(post("/employees/edit")
                .param("id", employee.getId().toString())
                .param("name", "edited")
                .param("role", "edited"))
                .andExpect(status().is(403));
    }

    @Test
    @WithAnonymousUser
    public void deleteEmployee_withAnonymousUser_mustReturnStatus403() throws Exception {
        Employee employee  = this.createEmployee();
        mockMvc.perform(post("/employees/delete")
                .param("id", employee.getId().toString()))
                .andExpect(status().is(403));
    }


}
