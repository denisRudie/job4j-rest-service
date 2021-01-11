package ru.job4j.auth.model;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonView(Views.EmpOnlyData.class)
    private int id;

    @JsonView(Views.EmpFull.class)
    private String firstName;

    @JsonView(Views.EmpFull.class)
    private String lastName;

    @JsonView(Views.EmpFull.class)
    private long inn;

    @JsonView(Views.EmpFull.class)
    private Date hired;

    @Transient
    @JsonView(Views.EmpFullAndAccData.class)
    private List<Person> accounts = new ArrayList<>();

    public static Employee of(int id, String firstName, String lastName, long inn) {
        Employee e = new Employee();
        e.id = id;
        e.firstName = firstName;
        e.lastName = lastName;
        e.inn = inn;
        return e;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public long getInn() {
        return inn;
    }

    public void setInn(long inn) {
        this.inn = inn;
    }

    public Date getHired() {
        return hired;
    }

    public void setHired(Date hired) {
        this.hired = hired;
    }

    public List<Person> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Person> accounts) {
        this.accounts = accounts;
    }

    public void addAccount(Person person) {
        this.accounts.add(person);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
