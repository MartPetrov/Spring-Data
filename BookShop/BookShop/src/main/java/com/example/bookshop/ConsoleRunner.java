package com.example.bookshop;

import com.example.bookshop.services.author.AuthorService;
import com.example.bookshop.services.book.BookService;
import com.example.bookshop.services.seed.SeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ConsoleRunner implements CommandLineRunner {
    private LocalDate BOOK_YEAR = LocalDate.of(2000,1,1);
    private LocalDate BOOK_YEAR_BEFORE = LocalDate.of(1990,1,1);



    private final SeedService seedService;
    private final BookService bookService;
    private final AuthorService authorService;

    @Autowired
    public ConsoleRunner(SeedService seedService, BookService bookService, AuthorService authorService) {
        this.seedService = seedService;
        this.bookService = bookService;
        this.authorService = authorService;
    }

    @Override
    public void run(String... args) throws Exception {
//    this.seedService.seedAuthors();
//    this.seedService.seedCategory();
//    this.seedService.seedBooks();
        this.seedService.seedAllData();
//    this.getAllBooksAfterAGivenYear();
//    this.getAllAuthorsWithBooksReleaseDateBefore();
//

    }

    private void getAllBooksAfterAGivenYear() {
        this.bookService.findAllByReleaseDateAfter(BOOK_YEAR).forEach(book -> System.out.println(book.getTitle()));
    }

    private void getAllAuthorsWithBooksReleaseDateBefore() {
        this.authorService.findDistinctByBooksBefore(BOOK_YEAR_BEFORE).forEach(author -> System.out.println(author.getFirstName() + " " + author.getLastName()));
    }
}
