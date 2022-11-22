package com.example.bookshop.services.author;

import com.example.bookshop.domain.entities.Author;
import com.example.bookshop.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public void seedAuthors(List<Author> authorList) {

        this.authorRepository.saveAll(authorList);
    }

    @Override
    public boolean isDataSeeded() {
        return this.authorRepository.count() > 0;
    }

    @Override
    public Author getRandomAuthor() {
        final long count = (int) this.authorRepository.count();
        if (count != 0) {
            final Long randomId = new Random().nextLong(1, count);
            return this.authorRepository.findAuthorById(randomId).orElseThrow(NoSuchElementException::new);
        }

        throw new RuntimeException();

    }

    @Override
    public List<Author> findDistinctByBooksBefore(LocalDate date) {
        return this.authorRepository.findDistinctByBooksReleaseDateBefore(date).orElseThrow(NoSuchElementException::new);
    }
}
