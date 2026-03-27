package com.example.demo.bai_tap_buoi_2.controller;

import com.example.demo.bai_tap_buoi_2.model.Book;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BookController {

    private List<Book> books = new ArrayList<>();
    private long currentId = 1;

    @PostConstruct
    public void init() {
        books.add(new Book(currentId++, "Clean Code", "Robert C. Martin", 40.0));
        books.add(new Book(currentId++, "Effective Java", "Joshua Bloch", 45.0));
    }

    @GetMapping
    public String listBooks(Model model) {
        model.addAttribute("books", books);
        return "books";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("book", new Book());
        return "add-book";
    }

    @PostMapping("/add")
    public String addBook(@ModelAttribute("book") Book book) {
        book.setId(currentId++);
        books.add(book);
        return "redirect:/books";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Optional<Book> bookOpt = books.stream().filter(b -> b.getId().equals(id)).findFirst();
        if (bookOpt.isPresent()) {
            model.addAttribute("book", bookOpt.get());
            return "edit-book";
        }
        return "redirect:/books";
    }

    @PostMapping("/edit/{id}")
    public String editBook(@PathVariable("id") Long id, @ModelAttribute("book") Book updatedBook) {
        for (int i = 0; i < books.size(); i++) {
            if (books.get(i).getId().equals(id)) {
                updatedBook.setId(id);
                books.set(i, updatedBook);
                break;
            }
        }
        return "redirect:/books";
    }

    @GetMapping("/delete/{id}")
    public String deleteBook(@PathVariable("id") Long id) {
        books.removeIf(b -> b.getId().equals(id));
        return "redirect:/books";
    }
}
