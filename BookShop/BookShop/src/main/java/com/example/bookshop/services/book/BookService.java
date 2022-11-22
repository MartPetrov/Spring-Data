package com.example.bookshop.services.book;

import com.example.bookshop.domain.entities.Book;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BookService {

    void seedBooks(List<Book> bookList);
   List<Book> findAllByReleaseDateAfter(LocalDate localDate);
}
