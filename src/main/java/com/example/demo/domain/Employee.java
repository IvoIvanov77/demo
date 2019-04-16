package com.example.demo.domain;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(name = "employees")
public class Employee extends BaseEntity {

    private String name;

    private String role;

    public Employee() {}

    public Employee(String name, String role) {
        this.name = name;
        this.role = role;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
