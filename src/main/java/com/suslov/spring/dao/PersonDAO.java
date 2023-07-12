package com.suslov.spring.dao;

import com.suslov.spring.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class PersonDAO {

    private final JdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Person> rowMapper = BeanPropertyRowMapper.newInstance(Person.class);

    @Autowired
    public PersonDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void save(Person person) {
        jdbcTemplate.update("INSERT INTO person(name,year) values(?,?) ", person.getName(), person.getYear());
    }

    public Person get(int id) {
        return jdbcTemplate.query("SELECT person_id as id, name, year FROM person WHERE person_id=?", rowMapper, id)
                .stream().findAny().orElse(null);
    }

    public List<Person> getAll() {
        return jdbcTemplate.query("SELECT person_id as id, name, year FROM person", rowMapper);
    }

    public void update(int id, Person person) {
        jdbcTemplate.update("UPDATE person SET name=?, year=? WHERE person_id=?", person.getName(), person.getYear(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM person WHERE person_id=?", id);
    }

    public Optional<Person> getByName(String name) {
        return jdbcTemplate.query("SELECT person_id as id, name, year FROM person WHERE name=?", rowMapper, name)
                .stream().findAny();
    }
}
