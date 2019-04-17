package com.example.demo.web.controller;

import com.example.demo.domain.entities.Employee;
import com.example.demo.domain.model.EmployeeDeleteBindingModel;
import com.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("employees")
@PreAuthorize("permitAll()")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("")
    public  Resources<Resource<Employee>> all(){
        return  this.employeeService.getAll();
    }

    @GetMapping("/{id}")
    public Resource<Employee> one(@PathVariable Long id){
        return  this.employeeService.get(id);
    }

    @PostMapping("/create")
    public Employee create(@RequestBody Employee employee){

        String debug ="";
        return  this.employeeService.createEmployee(employee);
    }


    @PostMapping("/edit")
    public Employee edit(@RequestBody Employee employee){
        return  this.employeeService.edit(employee);
    }

    @PostMapping("/delete")
    public Employee edit(@RequestBody EmployeeDeleteBindingModel bindingModel){
        return  this.employeeService.delete(bindingModel.getId());
    }




}
