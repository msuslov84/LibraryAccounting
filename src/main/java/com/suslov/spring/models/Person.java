package com.suslov.spring.models;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Entity
@Table(name = "person")
public class Person {

    @Id
    private int id;
    @NotEmpty(message = "Full name should not be empty")
    @Size(min = 3, max = 100, message = "Full name length should be between 3 and 100 characters")
    private String name;
    @Min(value = 1900, message = "Year of birth should be over 1900")
    private int year;

    public Person() {
    }

    public Person(String name, int year) {
        this.name = name;
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
