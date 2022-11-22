package com.example.bookshop.services.author;

import com.example.bookshop.domain.entities.Author;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AuthorService {
    void seedAuthors(List<Author> authorList);

    boolean isDataSeeded();

    Author getRandomAuthor();

    List<Author> findDistinctByBooksBefore(LocalDate date);

//    List<Author> findAuthorGroupByBooksOrderByBooks();

}
