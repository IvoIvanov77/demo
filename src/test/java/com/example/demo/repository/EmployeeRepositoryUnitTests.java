package com.example.demo.repository;


import com.example.demo.domain.entities.Employee;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class EmployeeRepositoryUnitTests
{

    @Autowired
    private EmployeeRepository employeeRepository;

    @Before
    public void seedDB(){
        this.employeeRepository.deleteAll();
        Employee[] employees = {
                new Employee("ABC", "ZZZ", new BigDecimal("100")),
                new Employee("ABD", "ZZY", new BigDecimal("1000")),
                new Employee("CAB", "ZWW", new BigDecimal("500")),
                new Employee("ACD", "ZDD", new BigDecimal("300")),
                new Employee("AAA", "MMM", new BigDecimal("200")),
                new Employee("ACD", "HKK", new BigDecimal("50"))
        };

        this.employeeRepository.saveAll(Arrays.asList(employees));
    }

    @Test
    public void search_byNameReturnCorrectCountOfItems(){
        List<Employee> resultList = this.employeeRepository.search("A", "",
                BigDecimal.ZERO,  new BigDecimal(Integer.MAX_VALUE));
        Assert.assertEquals(5, resultList.size());
    }

    @Test
    public void search_byNameReturnCorrectItemsInCorrectOrder(){
        List<Employee> resultList = this.employeeRepository.search("A", "",
                BigDecimal.ZERO, new BigDecimal(Integer.MAX_VALUE));
        Employee first = resultList.get(0);
        Employee last = resultList.get(resultList.size() - 1);
        Assert.assertEquals("ABD", first.getName());
        Assert.assertEquals("ACD", last.getName());
    }

    @Test
    public void search_bySalaryRangeReturnCorrectItemsInCorrectOrder(){
        List<Employee> resultList = this.employeeRepository.search("", "",
                new BigDecimal("200"), new BigDecimal("500"));
        Assert.assertEquals(3, resultList.size());
        Employee first = resultList.get(0);
        Employee last = resultList.get(resultList.size() - 1);
        Assert.assertEquals("CAB", first.getName());
        Assert.assertEquals("AAA", last.getName());
    }

    @Test
    public void search_byAllParametersReturnCorrectItemsInCorrectOrder(){
        List<Employee> resultList = this.employeeRepository.search("A", "Z",
                new BigDecimal("100"), new BigDecimal("300"));
        Assert.assertEquals(2, resultList.size());
        Employee first = resultList.get(0);
        Employee last = resultList.get(resultList.size() - 1);
        Assert.assertEquals("ACD", first.getName());
        Assert.assertEquals("ABC", last.getName());
    }
}
