package ru.job4j.auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.client.RestTemplate;
import ru.job4j.auth.AuthApplication;
import ru.job4j.auth.model.Employee;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.model.Views;
import ru.job4j.auth.repository.EmployeeRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = AuthApplication.class)
@AutoConfigureMockMvc
public class EmployeeControllerTest {

    private static final String API = "http://localhost:8080/person/";
    private static final String EMP_API_ID = "/employee/{id}";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private RestTemplate rest;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void findAll() throws Exception {
        List<Employee> employees = List.of(Employee.of(1, "Jack", "Johnson", 13321321312L));

        when(employeeRepository.findAll()).thenReturn(employees);

        Person p1 = Person.of(1, "login1", "pwd1");
        Person p2 = Person.of(2, "login2", "pwd2");
        p1.setEmployee(employees.get(0));
        p2.setEmployee(employees.get(0));
        List<Person> people = List.of(p1, p2);

        ResponseEntity<List<Person>> myEntity = new ResponseEntity<>(people, HttpStatus.ACCEPTED);

        when(rest.exchange(
                API,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Person>>() {
                })
        ).thenReturn(myEntity);

        MvcResult result = mockMvc.perform(get("/employee/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        String actualJson = result.getResponse().getContentAsString();

        String expectedJson = objectMapper
                .writerWithView(Views.EmpFullAndAccData.class)
                .writeValueAsString(employees);

        assertEquals(expectedJson, actualJson);
    }

    @Test
    public void addEmployeeAccount() throws Exception {
        Employee e = Employee.of(1, "Tom", "Johns", 1321321L);

        when(employeeRepository.findById(1)).thenReturn(Optional.of(e));

        Person newAccount = new Person();
        newAccount.setLogin("test");
        newAccount.setPassword("test");
        newAccount.setEmployee(e);

        Person createdAccount = new Person();
        createdAccount.setId(1);
        createdAccount.setLogin("test");
        newAccount.setPassword("test");
        newAccount.setEmployee(e);
        String newAccJson = objectMapper.writerWithView(Views.AccFull.class)
                .writeValueAsString(createdAccount);

        when(rest.postForObject(API, newAccount, Person.class)).thenReturn(createdAccount);

        mockMvc.perform(post("/employee/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newAccount)))
                .andExpect(status().isCreated())
                .andExpect(content().json(newAccJson));
    }

    @Test
    public void updateEmployeeAccount() throws Exception {
        Employee e = Employee.of(1, "Tom", "Johns", 1321321L);

        when(employeeRepository.existsById(1)).thenReturn(true);

        Person newAccount = new Person();
        newAccount.setId(1);
        newAccount.setLogin("test");
        newAccount.setPassword("test");
        newAccount.setEmployee(e);

        doNothing().when(rest).put(anyString(), any());

        mockMvc.perform(put("/employee/1")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(newAccount)))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteEmployeeAccount() throws Exception {
        doNothing().when(rest).delete(anyString(), anyInt());
        mockMvc.perform(delete(EMP_API_ID, 1))
                .andExpect(status().isOk());
    }
}

