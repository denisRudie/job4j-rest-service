package ru.job4j.auth.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import ru.job4j.auth.model.Employee;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.model.Views;
import ru.job4j.auth.repository.EmployeeRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    private static final String API = "http://localhost:8080/person/";
    private static final String API_ID = "http://localhost:8080/person/{id}";

    private final RestTemplate restTemplate;
    private final EmployeeRepository repository;

    @Autowired
    public EmployeeController(RestTemplate restTemplate, EmployeeRepository repository) {
        this.restTemplate = restTemplate;
        this.repository = repository;
    }

    @GetMapping("/")
    @JsonView(Views.EmpFullAndAccData.class)
    public List<Employee> findAll() {
        Map<Integer, Employee> employees = StreamSupport
                .stream(repository.findAll().spliterator(), false)
                .collect(Collectors.toMap(Employee::getId, employee -> employee));

        List<Person> persons = restTemplate.exchange(
                API, HttpMethod.GET,
                null, new ParameterizedTypeReference<List<Person>>() {
                }).getBody();

        for (Person person : persons) {
            employees.get(person.getEmployee().getId()).getAccounts().add(person);
        }
        return new ArrayList<>(employees.values());
    }

    @PostMapping("/{id}")
    public ResponseEntity<Person> addEmployeeAccount(@PathVariable("id") int empId,
                                                     @RequestBody Person account) {
        if (empId == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (repository.findById(empId).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Employee e = new Employee();
        e.setId(empId);
        account.setEmployee(e);
        Person createdPerson = restTemplate.postForObject(API, account, Person.class);
        return new ResponseEntity<>(createdPerson, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateEmployeeAccount(@PathVariable("id") int empId,
                                                      @RequestBody Person account) {
        if (empId == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if (!repository.existsById(empId)) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        Employee e = new Employee();
        e.setId(empId);
        account.setEmployee(e);

        restTemplate.put(API, account);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccountFromEmployee(@PathVariable("id") int accountId) {
        if (accountId == 0) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        restTemplate.delete(API_ID, accountId);
        return ResponseEntity.ok().build();
    }
}
