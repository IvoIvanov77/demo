package com.example.demo.repository;

import com.example.demo.domain.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long>
{

    @Query("" +
            "select e from Employee e where e.name like :name% " +
            "and e.role like :role% " +
            "and e.salary >= :minSalary " +
            "and e.salary <= :maxSalary order by e.salary desc")
    List<Employee> search(@Param("name") String name, @Param("role") String role,
                          @Param("minSalary") BigDecimal minSalary, @Param("maxSalary") BigDecimal maxSalary);


}
