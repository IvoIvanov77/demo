package com.example.demo.web.controller;

import com.example.demo.domain.entities.Employee;
import com.example.demo.domain.model.EmployeeDeleteBindingModel;
import com.example.demo.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping(produces = "application/json")
    public  Resources<Resource<Employee>> all(){
        return  this.employeeService.getAll();
    }

    @GetMapping(path = "/{id}", produces = "application/json")
    public Resource<Employee> one(@PathVariable Long id){
        return  this.employeeService.get(id);
    }

    @PostMapping("/create")
    public ResponseEntity<Employee> create(@RequestBody Employee employee){
        return new ResponseEntity<>(this.employeeService.createEmployee(employee), HttpStatus.CREATED);
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
