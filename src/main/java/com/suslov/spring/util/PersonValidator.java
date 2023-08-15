package com.suslov.spring.util;

import com.suslov.spring.models.Person;
import com.suslov.spring.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class PersonValidator implements Validator {

    private final PersonService service;

    @Autowired
    public PersonValidator(PersonService service) {
        this.service = service;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        if (service.getByName(person.getFullName()).isPresent()) {
            errors.rejectValue("name", "", "There already exists a person with this full name");
        }
    }
}
