package com.example.bookshop.services.seed;

import com.example.bookshop.domain.entities.Author;
import com.example.bookshop.domain.entities.Book;
import com.example.bookshop.domain.entities.Category;
import com.example.bookshop.domain.enums.AgeRestriction;
import com.example.bookshop.domain.enums.EditionType;
import com.example.bookshop.services.author.AuthorService;
import com.example.bookshop.services.book.BookService;
import com.example.bookshop.services.categories.CategoryService;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.bookshop.Constans.FilePath.*;

@Component
public class SeedServiceImpl implements SeedService {

    private final AuthorService authorService;
    private final BookService bookService;
    private final CategoryService categoryService;

    public SeedServiceImpl(AuthorService authorService, BookService bookService, CategoryService categoryService) {
        this.authorService = authorService;
        this.bookService = bookService;
        this.categoryService = categoryService;
    }


    @Override
    public void seedAuthors() throws IOException {
        if (!this.authorService.isDataSeeded()) {
            this.authorService.seedAuthors(Files.readAllLines(Path.of(RESOURCE_URL + AUTHOR_FILE_NAME))
                    .stream().filter(s -> !s.isBlank()).map(firstAndLastName ->
                            Author.builder()
                                    .firstName(firstAndLastName.split(" ")[0])
                                    .lastName(firstAndLastName.split(" ")[1])
                                    .build()).collect(Collectors.toList()));
        }
    }

    @Override
    public void seedBooks() throws IOException {
       final List<Book> books = Files.readAllLines(Path.of(RESOURCE_URL + BOOK_FILE_NAME))
                .stream().filter(s -> !s.isBlank()).map(row -> {
            String[] data = row.split("\\s+");


            return Book.builder()
                    .title(Arrays.stream(data).skip(5).collect(Collectors.joining(" ")))
                    .editionType(EditionType.values()[Integer.parseInt(data[0])])
                    .prize(new BigDecimal(data[3]))
                    .releaseDate(LocalDate.parse(data[1], DateTimeFormatter.ofPattern("d/M/yyyy")))
                    .ageRestriction(AgeRestriction.values()[Integer.parseInt(data[4])])
                    .author(this.authorService.getRandomAuthor())
                    .categories(categoryService.getRandomCategories())
                    .copies(Integer.parseInt(data[2]))
                    .build();
        }).collect(Collectors.toList());

        this.bookService.seedBooks(books);

    }

    @Override
    public void seedCategory() throws IOException {
        if (!this.categoryService.isDataSeeded()) {
            this.categoryService.seedCategory(Files.readAllLines(Path.of(RESOURCE_URL + CATEGORY_FILE_NAME))
                    .stream()
                    .filter(s -> !s.isBlank())
                    .map(name -> Category.builder()
                            .name(name)
                            .build())
                    .collect(Collectors.toList()));
        }
    }
}
