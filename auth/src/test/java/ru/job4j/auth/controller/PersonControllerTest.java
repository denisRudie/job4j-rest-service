package ru.job4j.auth.controller;

import com.google.gson.Gson;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.job4j.auth.AuthApplication;
import ru.job4j.auth.model.Person;
import ru.job4j.auth.repository.PersonRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest(classes = AuthApplication.class)
@AutoConfigureMockMvc
public class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonRepository repository;

    @Autowired
    private Gson gson;

    /**
     * Testing {@link PersonController#create(Person)}.
     */
    @Test
    public void whenCreatePersonThenGetCreatedPerson() throws Exception {
        Person person = new Person();
        person.setLogin("test");
        person.setPassword("pwd");
        String jsonOut = gson.toJson(person);

        mockMvc.perform(post("/person/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonOut))
                .andExpect(status().isCreated());

        ArgumentCaptor<Person> personArg = ArgumentCaptor.forClass(Person.class);
        verify(repository).save(personArg.capture());

        assertEquals("test", personArg.getValue().getLogin());
        assertEquals("pwd", personArg.getValue().getPassword());
    }

    /**
     * Testing {@link PersonController#findAll()}.
     */
    @Test
    public void whenGetAllPersonsThenReturnListOfPersons() throws Exception {
        List<Person> persons = List.of(
                Person.of(1, "test1", "pwd1"),
                Person.of(2, "test2", "pwd2"),
                Person.of(3, "test3", "pwd3")
        );

        String jsonOut = gson.toJson(persons);

        when(repository.findAll()).thenReturn(persons);

        mockMvc.perform(get("/person/"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonOut));

        verify(repository).findAll();
    }

    /**
     * Testing {@link PersonController#findById(int)} case of existing data.
     */
    @Test
    public void whenFindExistingPersonByIdThenReturnPerson() throws Exception {
        Person person = Person.of(1, "login", "pwd");

        when(repository.findById(1)).thenReturn(Optional.of(person));

        String jsonOut = gson.toJson(person);

        mockMvc.perform(get("/person/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonOut));

        verify(repository).findById(anyInt());
    }

    /**
     * Testing {@link PersonController#findById(int)} case of missing data.
     */
    @Test
    public void whenFindMissingPersonByIdThenReturnError() throws Exception {
        String jsonOut = gson.toJson(new Person());

        mockMvc.perform(get("/person/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json(jsonOut));

        verify(repository).findById(anyInt());
    }

    /**
     * Testing {@link PersonController#delete(int)}.
     */
    @Test
    public void whenDeletePersonThenGetOkStatus() throws Exception {
        mockMvc.perform(delete("/person/1"))
                .andExpect(status().isOk());

        verify(repository).delete(any());
    }

    @Test
    public void whenUpdateExistingPersonThenGetOkStatus() throws Exception {
        String updatedPerson = gson.toJson(
                Person.of(1, "upd", "pwd"));

        mockMvc.perform(put("/person/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(updatedPerson))
                .andExpect(status().isOk());

        verify(repository).save(any());
    }
}
