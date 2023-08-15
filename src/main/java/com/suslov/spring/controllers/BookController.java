package com.suslov.spring.controllers;

import com.suslov.spring.models.Book;
import com.suslov.spring.models.Person;
import com.suslov.spring.services.BookService;
import com.suslov.spring.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;
    private final PersonService personService;

    @Autowired
    public BookController(BookService bookService, PersonService personService) {
        this.bookService = bookService;
        this.personService = personService;
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(value = "sort_by_year", required = false) Boolean sortByYear) {
        boolean sorted = sortByYear != null && sortByYear;
        if (page == null || booksPerPage == null) {
            model.addAttribute("books", bookService.findAll(sorted));
        } else {
            model.addAttribute("books", bookService.findAllWithPagination(booksPerPage, page, sorted));
        }
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(Model model,
                       @PathVariable("id") int id,
                       @ModelAttribute("person") Person person) {
        Optional<Person> owner = bookService.getOwner(id);
        if (owner.isPresent()) {
            model.addAttribute("owner", owner.get());
        } else {
            model.addAttribute("people", personService.findAll());
        }
        model.addAttribute("book", bookService.findById(id));
        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        return "books/new";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("book", bookService.findById(id));
        return "books/edit";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") int id) {
        bookService.delete(id);
        return "redirect:/books";
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam(value = "query", required = false) String query) {
        if (query != null) {
            model.addAttribute("books", bookService.findByTitle(query));
        }

        return "books/search";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "books/new";
        }
        bookService.save(book);
        return "redirect:/books";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult,
                         @PathVariable("id") int id) {
        if (bindingResult.hasErrors()) {
            return "books/edit";
        }
        bookService.update(id, book);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/assign")
    public String assign(@ModelAttribute("person") Person person,
                         @PathVariable("id") int id) {
        bookService.assign(id, person);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id) {
        bookService.release(id);
        return "redirect:/books/" + id;
    }

//    @DeleteMapping("/{id}")
//    public String delete(@PathVariable("id") int id) {
//        bookService.delete(id);
//        return "redirect:/books";
//    }
}
