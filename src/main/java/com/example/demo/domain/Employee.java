package com.example.demo.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@lombok.Data
@Entity
public class Employee {

    private @Id
    @GeneratedValue
    Long id;
    private String name;
    private String role;

    public Employee(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public Employee() {
    }
}
