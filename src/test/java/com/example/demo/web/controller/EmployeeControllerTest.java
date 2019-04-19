package com.example.demo.web.controller;


import com.example.demo.domain.entities.Employee;
import com.example.demo.domain.model.EmployeeDeleteBindingModel;
import com.example.demo.repository.EmployeeRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EmployeeControllerTest {

    public static final String EMPLOYEE_NAME = "ivaylo";
    public static final String EMPLOYEE_ROLE = "junior";
    public static final String EDITED_NAME = "editedName";
    public static final String EDITED_ROLE = "editedRole";
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeRepository employeeRepository;


    @Before
    public void setUp() {
       employeeRepository.deleteAll();
    }

    private Employee createEmployee(){
        return new Employee(EMPLOYEE_NAME, EMPLOYEE_ROLE);
    }

    private Employee addEmployeeInDB() {
        Employee employee = createEmployee();

        return employeeRepository.save(employee);
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    //get all
    @Test
    @WithMockUser
    public void getEmployees_withLoggedInUser_mustReturnStatus200() throws Exception {
        this.addEmployeeInDB();
        mockMvc.perform(get("/employees/")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$._embedded.employeeList[0].name")
                        .value(EMPLOYEE_NAME));
    }

    @Test
    @WithAnonymousUser
    public void getEmployees_withAnonymousUser_mustReturnStatus403() throws Exception {
        this.addEmployeeInDB();
        mockMvc.perform(get("/employees"))
                .andExpect(status().is(403));
    }

    //get one
    @Test
    @WithMockUser
    public void getEmployeeById_withLoggedInUser_mustReturnStatus200() throws Exception {
        Employee employee  = this.addEmployeeInDB();
        mockMvc.perform(get("/employees/" + employee.getId())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name")
                        .value(EMPLOYEE_NAME));
    }

    @Test
    @WithMockUser
    public void getEmployeeById_withLoggedInUserAndInvalidId_mustReturnStatus404() throws Exception {
        Employee employee  = this.addEmployeeInDB();
        mockMvc.perform(get("/employees/" + 100)
                .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().is(404));
    }

    @Test
    @WithAnonymousUser
    public void getEmployeeById_withAnonymousUser_mustReturnStatus403() throws Exception {
        Employee employee  = this.addEmployeeInDB();
        mockMvc.perform(get("/employees/" + employee.getId()))
                .andExpect(status().is(403));
    }

    //create
    @Test
    @WithMockUser(roles={"USER","ADMIN"})
    public void createEmployee_withLoggedInAdmin_mustReturnStatus201() throws Exception {
        Employee employee = createEmployee();
        mockMvc.perform(post("/employees/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(employee)))
                .andExpect(status().is(201))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name")
                        .value(EMPLOYEE_NAME));
    }

    @Test
    @WithMockUser(roles={"USER","ADMIN"})
    public void createEmployee_withLoggedInAdmin_mustCreateEmployeeInDB() throws Exception {
        Employee employee = createEmployee();
        mockMvc.perform(post("/employees/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(employee)));

        Assert.assertEquals(1, employeeRepository.count());
    }

    @Test
    @WithMockUser()
    public void createEmployee_withLoggedInRegularUser_mustReturnStatus403() throws Exception {
        Employee employee = createEmployee();
        mockMvc.perform(post("/employees/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(employee)))
                .andExpect(status().is(403));
    }

    @Test
    @WithAnonymousUser
    public void createEmployee_withAnonymousUser_mustReturnStatus403() throws Exception {
        Employee employee = createEmployee();
        mockMvc.perform(post("/employees/create")
                .content(asJsonString(employee)))
                .andExpect(status().is(403));
    }

    //edit
    @Test
    @WithMockUser(roles={"USER","ADMIN"})
    public void editEmployee_withLoggedInAdmin_mustReturnStatus201() throws Exception {
        Employee employee = addEmployeeInDB();
        Employee toEdit = new Employee(EDITED_NAME, EDITED_ROLE);
        toEdit.setId(employee.getId());

        mockMvc.perform(post("/employees/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(toEdit)))
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name")
                .value(EDITED_NAME));
    }

    @Test
    @WithMockUser(roles={"USER","ADMIN"})
    public void editEmployee_withLoggedInAdmin_mustEditEmployeeInDB() throws Exception {
        Employee employee = addEmployeeInDB();
        Employee toEdit = new Employee(EDITED_NAME, EDITED_ROLE);
        toEdit.setId(employee.getId());

        mockMvc.perform(post("/employees/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(toEdit)));

        String actualName = employeeRepository.findAll().get(0).getName();
        Assert.assertEquals(EDITED_NAME, actualName);
    }

    @Test
    @WithMockUser()
    public void editEmployee_withLoggedInRegularUser_mustReturnStatus403() throws Exception {
        Employee employee = addEmployeeInDB();
        Employee toEdit = new Employee(EDITED_NAME, EDITED_ROLE);
        toEdit.setId(employee.getId());

        mockMvc.perform(post("/employees/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(toEdit)))
                .andExpect(status().is(403));
    }

    @Test
    @WithAnonymousUser
    public void editEmployee_withAnonymousUser_mustReturnStatus403() throws Exception {
        Employee employee = addEmployeeInDB();
        Employee toEdit = new Employee(EDITED_NAME, EDITED_ROLE);
        toEdit.setId(employee.getId());

        mockMvc.perform(post("/employees/edit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(toEdit)))
                .andExpect(status().is(403));
    }

    //delete
    @Test
    @WithMockUser(roles={"USER","ADMIN"})
    public void deleteEmployee_withLoggedInAdmin_mustReturnStatus200() throws Exception {
        Employee employee = addEmployeeInDB();
        EmployeeDeleteBindingModel toDelete = new EmployeeDeleteBindingModel();
        toDelete.setId(employee.getId());

        mockMvc.perform(post("/employees/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(toDelete)))
                .andExpect(status().is(200))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name")
                        .value(EMPLOYEE_NAME));
    }

    @Test
    @WithMockUser(roles={"USER","ADMIN"})
    public void deleteEmployee_withLoggedInAdmin_mustDeleteEmployeeInDB() throws Exception {
        Employee employee = addEmployeeInDB();
        Assert.assertEquals(1, employeeRepository.count());

        EmployeeDeleteBindingModel toDelete = new EmployeeDeleteBindingModel();
        toDelete.setId(employee.getId());

        mockMvc.perform(post("/employees/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(toDelete)));

        Assert.assertEquals(0, employeeRepository.count());
    }

    @Test
    @WithMockUser()
    public void deleteEmployee_withLoggedInRegularUser_mustReturnStatus403() throws Exception {
        Employee employee = addEmployeeInDB();
        EmployeeDeleteBindingModel toDelete = new EmployeeDeleteBindingModel();
        toDelete.setId(employee.getId());

        mockMvc.perform(post("/employees/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(toDelete)))
                .andExpect(status().is(403));
    }

    @Test
    @WithAnonymousUser
    public void deleteEmployee_withAnonymousUser_mustReturnStatus403() throws Exception {
        Employee employee = addEmployeeInDB();
        EmployeeDeleteBindingModel toDelete = new EmployeeDeleteBindingModel();
        toDelete.setId(employee.getId());

        mockMvc.perform(post("/employees/delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(toDelete)))
                .andExpect(status().is(403));
    }



}
