package ru.job4j.auth.model;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "account")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.AccFull.class)
    private int id;

    @JsonView(Views.AccOnlyData.class)
    private String login;

    @JsonView(Views.AccOnlyData.class)
    private String password;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @JsonView(Views.AccFull.class)
    private Employee employee;

    public static Person of(int id, String login, String pwd) {
        Person person = new Person();
        person.id = id;
        person.login = login;
        person.password = pwd;
        return person;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
