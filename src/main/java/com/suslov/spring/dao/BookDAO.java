package com.suslov.spring.dao;

import com.suslov.spring.models.Book;
import com.suslov.spring.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {

    private final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Book> ROW_MAPPER = BeanPropertyRowMapper.newInstance(Book.class);

    @Autowired
    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO book(title, author, year) values(?, ?, ?) ", book.getTitle(), book.getAuthor(), book.getYear());
    }

    public Book get(int id) {
        return jdbcTemplate.query("SELECT book_id as id, title, author, year, person_id FROM book WHERE book_id=?", ROW_MAPPER, id)
                .stream().findAny().orElse(null);
    }

    public List<Book> getAll() {
        return jdbcTemplate.query("SELECT book_id as id, title, author, year, person_id FROM book", ROW_MAPPER);
    }

    public void update(int id, Book book) {
        jdbcTemplate.update("UPDATE book SET title=?, author=?, year=? WHERE book_id=?",
                book.getTitle(), book.getAuthor(), book.getYear(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM book WHERE book_id=?", id);
    }

    public void assign(int id_book, int id_person) {
        jdbcTemplate.update("UPDATE book SET person_id=? WHERE book_id=?", id_person, id_book);
    }

    public void release(int id) {
        jdbcTemplate.update("UPDATE book SET person_id=NULL WHERE book_id=?", id);
    }

    public Optional<Person> getOwner(int id) {
        return jdbcTemplate.query("SELECT p.person_id as id, p.name, p.year " +
                "FROM person AS p INNER JOIN book AS b " +
                "ON p.person_id = b.person_id " +
                "WHERE book_id=?", new BeanPropertyRowMapper<>(Person.class), id).stream().findAny();
    }

    public List<Book> getAllByOwner(int id_person) {
        return jdbcTemplate.query("SELECT book_id as id, title, author, year, person_id FROM book WHERE person_id=?", ROW_MAPPER, id_person);
    }
}
