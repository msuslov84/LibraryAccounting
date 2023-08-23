package com.suslov.spring.services;

import com.suslov.spring.models.Book;
import com.suslov.spring.models.Person;
import com.suslov.spring.repositories.PersonRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PersonService {

    private static final int DAYS_OF_OVERDUE = 10;
    private static final int MILLISECONDS_IN_DAY = 86_400_000;

    private final PersonRepository repository;

    @Autowired
    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }


    public List<Person> findAll() {
        return repository.findAll();
    }

    public Person findById(int id) {
        return repository.findById(id)
                .orElse(null);
    }

    @Transactional
    public void save(Person person) {
        repository.save(person);
    }

    @Transactional
    public void update(int id, Person updatedPerson) {
        updatedPerson.setId(id);
        repository.save(updatedPerson);
    }

    @Transactional
    public void delete(int id) {
        repository.deleteById(id);
    }

    public Optional<Person> getByName(String name) {
        return repository.findByFullName(name);
    }

    public List<Book> getBooksByPersonId(int id) {
        return repository.findById(id)
                // Get books assigned to the reader and determine if they are overdue
                .map(p -> {
                    List<Book> books = p.getBooks();
                    Hibernate.initialize(books);
                    books.forEach(b -> {
                        long timeAfterAssign = new Date().getTime() - b.getAssignedFrom().getTime();
                        b.setOverdue(timeAfterAssign / MILLISECONDS_IN_DAY > DAYS_OF_OVERDUE);
                    });
                    return books;
                })
                .orElse(Collections.emptyList());
    }
}