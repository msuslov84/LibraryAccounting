package com.suslov.spring.services;

import com.suslov.spring.models.Book;
import com.suslov.spring.models.Person;
import com.suslov.spring.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BookService {

    private final BookRepository repository;

    @Autowired
    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public List<Book> findAll(boolean sort_by_year) {
        if (sort_by_year) {
            return repository.findAll(Sort.by("year"));
        } else {
            return repository.findAll();
        }
    }

    public List<Book> findAllWithPagination(int books_per_page, int page, boolean sort_by_year) {
        if (sort_by_year) {
            return repository.findAll(PageRequest.of(page, books_per_page, Sort.by("year"))).getContent();
        } else {
            return repository.findAll(PageRequest.of(page, books_per_page)).getContent();
        }
    }

    public Book findById(int id) {
        return repository.findById(id).orElse(null);
    }

    public List<Book> findByTitle(String title) {
        return repository.findByTitleStartingWithIgnoreCase(title);
    }

    @Transactional
    public void save(Book book) {
        repository.save(book);
    }

    @Transactional
    public void update(int id, Book updatedBook) {
        updatedBook.setId(id);
        repository.save(updatedBook);
    }

    @Transactional
    public void delete(int id) {
        repository.deleteById(id);
    }

    @Transactional
    public void assign(int id, Person person) {
        repository.findById(id).ifPresent(b -> {
            b.setOwner(person);
            b.setAssignedFrom(new Date());
        });
    }

    public Optional<Person> getOwner(int id) {
        return repository.findById(id).map(Book::getOwner);
    }

    @Transactional
    public void release(int id) {
        repository.findById(id).ifPresent(b -> {
            b.setOwner(null);
            b.setAssignedFrom(null);
        });
    }
}
