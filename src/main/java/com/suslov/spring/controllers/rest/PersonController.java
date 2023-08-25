package com.suslov.spring.controllers.rest;

import com.suslov.spring.models.Person;
import com.suslov.spring.services.PersonService;
import com.suslov.spring.util.PersonValidator;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/readers")
public class PersonController {

    private final PersonService service;
    private final PersonValidator validator;

    @Autowired
    public PersonController(PersonService service, PersonValidator validator) {
        this.service = service;
        this.validator = validator;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", service.findAll());
        return "readers/index";
    }

    @GetMapping("/{id}")
    public String show(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", service.findById(id));
        model.addAttribute("books", service.getBooksByPersonId(id));
        return "readers/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        return "readers/new";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", service.findById(id));
        return "readers/edit";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        service.delete(id);
        return "redirect:/readers";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {
        validator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            return "readers/new";
        }
        service.save(person);
        return "redirect:/readers";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "readers/edit";
        }
        service.update(id, person);
        return "redirect:/readers";
    }
}