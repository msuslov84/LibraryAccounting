package com.suslov.spring.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.Date;

@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private int id;

    @NotEmpty(message = "Title should not be empty")
    @Size(min = 2, max = 100, message = "Title length should be between 2 and 100 characters")
    @Column(name = "title")
    private String title;

    @NotEmpty(message = "Author name should be filled")
    @Size(min = 2, max = 100, message = "Author name length should be between 2 and 100 characters")
    @Column(name = "author")
    private String author;

    @Min(value = 1500, message = "Year of publication should be over 1500")
    @Column(name = "year")
    private int publicationYear;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "person_id")
    private Person owner;

    @Column(name = "assigned_from")
    @Temporal(TemporalType.TIMESTAMP)
    private Date assignedFrom;

    @Transient
    private boolean overdue;

    public Book() {
    }

    public Book(String title, String author, int publicationYear) {
        this.title = title;
        this.author = author;
        this.publicationYear = publicationYear;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getPublicationYear() {
        return publicationYear;
    }

    public void setPublicationYear(int publicationYear) {
        this.publicationYear = publicationYear;
    }

    public Person getOwner() {
        return owner;
    }

    public void setOwner(Person owner) {
        this.owner = owner;
    }

    public Date getAssignedFrom() {
        return assignedFrom;
    }

    public void setAssignedFrom(Date assignedFrom) {
        this.assignedFrom = assignedFrom;
    }

    public boolean isOverdue() {
        return overdue;
    }

    public void setOverdue(boolean overdue) {
        this.overdue = overdue;
    }
}