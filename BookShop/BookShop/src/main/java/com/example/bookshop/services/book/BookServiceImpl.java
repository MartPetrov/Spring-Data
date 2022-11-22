package com.example.bookshop.services.book;

import com.example.bookshop.domain.entities.Book;
import com.example.bookshop.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BookServiceImpl implements BookService {
    private final BookRepository bookRepository;

    @Autowired
    public BookServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void seedBooks(List<Book> bookList) {
        this.bookRepository.saveAll(bookList);
    }

    @Override
    public List<Book> findAllByReleaseDateAfter(LocalDate localDate) {
        return this.bookRepository.findAllByReleaseDateAfter(localDate).orElseThrow(NoSuchElementException::new);
    }
}
